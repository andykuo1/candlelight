package org.zilar.renderer;

import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.window.Window;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.renderer.Drawable;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.renderer.RenderService;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyShadow;
import org.zilar.property.PropertySpecular;
import org.zilar.property.PropertyTexture;
import org.zilar.renderer.shadow.DynamicLight;
import org.zilar.renderer.shadow.ShadowBox;
import org.zilar.renderer.shadow.ShadowRenderer;
import org.zilar.sprite.Sprite;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 6/7/17.
 */
public class DiffuseRenderer extends RenderService
{
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	private ShadowRenderer shadowRenderer;

	private AssetManager assetManager;
	private Window window;
	private List<DynamicLight> lights;
	private PerspectiveCamera camera;

	public DiffuseRenderer(AssetManager assetManager, Window window, PerspectiveCamera camera, List<DynamicLight> lights, Asset<Program> program)
	{
		this.program = program;
		this.window = window;
		this.lights = lights;
		this.camera = camera;

		this.shadowRenderer = new ShadowRenderer(this.window);
		this.assetManager = assetManager;
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
		this.shadowRenderer.load(this.camera, this.assetManager);
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
		this.shadowRenderer.unload();
	}

	public void preRender(PerspectiveCamera camera, Iterator<Drawable> iterator)
	{
		this.shadowRenderer.render(iterator, this.lights.get(2));
	}

	public void render(Camera camera, Iterator<Drawable> iterator)
	{
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final Drawable inst = iterator.next();
				if (!inst.isVisible()) continue;

				final Model model = inst.getModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				Texture texture = null;
				Sprite sprite = null;
				Vector3f specularColor = null;
				float shininess = 0;
				boolean shadow = false;

				if (material.hasComponent(PropertyDiffuse.class))
				{
					PropertyDiffuse propDiffuse = material.getComponent(PropertyDiffuse.class);
					program.setUniform("u_diffuse_color", propDiffuse.diffuseColor);
				}

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.getTexture().getSource();
					sprite = propTexture.getSprite();
					program.setUniform("u_transparency", propTexture.transparent);
				}

				if (material.hasComponent(PropertySpecular.class))
				{
					PropertySpecular propSpecular = material.getComponent(PropertySpecular.class);
					specularColor = propSpecular.specularColor;
					shininess = propSpecular.shininess;
				}

				if (material.hasComponent(PropertyShadow.class))
				{
					shadow = material.getComponent(PropertyShadow.class).receiveShadow;
					if (shadow)
					{
						program.setUniform("u_shadow_transform", this.shadowRenderer.getToShadowMapSpaceMatrix());
						program.setUniform("u_shadow_sampler", 1);
						program.setUniform("u_shadow_dist", ShadowBox.SHADOW_DISTANCE);
						program.setUniform("u_shadow_transition", ShadowBox.SHADOW_DISTANCE / 10F);
						program.setUniform("u_shadow_map_size", (float) ShadowRenderer.SHADOW_MAP_SIZE);
						program.setUniform("u_pcf_kernel_size", 2);
					}
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				program.setUniform("u_model", transformation);

				Matrix4fc modelView = view.mul(transformation, this.modelViewMatrix);
				Matrix4fc modelViewProj = proj.mul(modelView, this.modelViewProjMatrix);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					if (texture != null)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						program.setUniform("u_sampler", 0);
						texture.bind();
					}

					program.setUniform("u_camera_pos", camera.transform().position3());

					if (sprite != null)
					{
						program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
						program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));
					}

					if (specularColor != null)
					{
						program.setUniform("u_shininess", shininess);
						program.setUniform("u_specular_color", specularColor);
					}

					bindLightsToProgram(program, this.lights);

					if (shadow)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE1);
						this.shadowRenderer.getShadowFBO().getTexture().bind();
					}

					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

					if (shadow)
					{
						this.shadowRenderer.getShadowFBO().getTexture().unbind();
					}

					if (texture != null)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.unbind();
					}
				}
				mesh.unbind();
			}
		}
		program.unbind();
	}

	private static void bindLightsToProgram(Program program, Collection<DynamicLight> lights)
	{
		program.setUniform("u_light_count", lights.size());

		int i = 0;
		Iterator<DynamicLight> iter = lights.iterator();
		while (iter.hasNext())
		{
			DynamicLight light = iter.next();
			program.setUniform(lightUniform("position", i), light.position);
			program.setUniform(lightUniform("intensity", i), light.color);
			program.setUniform(lightUniform("attenuation", i), light.attentuation);
			program.setUniform(lightUniform("ambientCoefficient", i), light.ambientCoefficient);
			program.setUniform(lightUniform("coneAngle", i), light.coneAngle);
			program.setUniform(lightUniform("coneDirection", i), light.coneDirection);
			++i;
		}
	}

	private static String lightUniform(String name, int index)
	{
		return "u_light[" + index + "]." + name;
	}
}
