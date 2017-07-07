package org.zilar.renderer.shadow;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.material.Material;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.FBO;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.window.Window;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.renderer.Renderable;
import org.zilar.model.Model;
import org.zilar.property.PropertyShadow;
import org.zilar.property.PropertyTexture;
import org.zilar.resource.ResourceLocation;
import org.zilar.resourceloader.ProgramLoader;
import org.zilar.resourceloader.ShaderLoader;
import org.zilar.resourceloader.TextureLoader;
import org.zilar.sprite.Sprite;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class ShadowRenderer
{
	public static final int SHADOW_MAP_SIZE = 4096;
	private Matrix4f modelViewProjMatrix = new Matrix4f();
	private Matrix4f modelViewMatrix = new Matrix4f();
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f lightProjMatrix = new Matrix4f();
	private Matrix4f lightViewProjMatrix = new Matrix4f();
	private Matrix4f offset = new Matrix4f().translate(0.5F, 0.5F, 0.5F).scale(0.5F, 0.5F, 0.5F);

	private ShadowBox shadowBox;
	private FBO shadowFBO;

	private Asset<Texture> shadowMap;
	private Asset<Program> shadowProgram;

	private PerspectiveCamera camera;
	private Window window;

	public ShadowRenderer(PerspectiveCamera camera, Window window)
	{
		this.window = window;
		this.camera = camera;
	}

	public void load(AssetManager assetManager)
	{
		this.shadowBox = new ShadowBox(this.lightViewMatrix, camera, window);
		this.shadowMap = assetManager.registerAsset(Texture.class, "shadowmap",
				new TextureLoader.TextureParameter(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE, Bitmap.Format.DEPTH));
		this.shadowFBO = new FBO(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE, false);
		this.shadowFBO.attachTexture(this.shadowMap.getSource(), GL30.GL_DEPTH_ATTACHMENT, true);

		Asset<Shader> vs = assetManager.registerAsset(Shader.class, "v_shadow", new ShaderLoader.ShaderParameter(new ResourceLocation("glim:shader/shadow.vsh"), GL20.GL_VERTEX_SHADER));
		Asset<Shader> fs = assetManager.registerAsset(Shader.class, "f_shadow", new ShaderLoader.ShaderParameter(new ResourceLocation("glim:shader/shadow.fsh"), GL20.GL_FRAGMENT_SHADER));
		this.shadowProgram = assetManager.registerAsset(Program.class, "shadow", new ProgramLoader.ProgramParameter(Arrays.asList(vs, fs)));
	}

	public void unload()
	{
		this.shadowFBO.close();
	}

	public void render(Iterator<Renderable> instances, DynamicLight light)
	{
		this.shadowBox.update();

		Matrix4fc proj = updateOrthoProjection(shadowBox.getWidth(), shadowBox.getHeight(), shadowBox.getLength(), this.lightProjMatrix);
		Vector3f dir = new Vector3f(light.position.x, light.position.y, light.position.z).mul(-1);
		Matrix4fc view = updateLightView(dir, shadowBox.getCenter(), this.lightViewMatrix);
		this.lightViewProjMatrix.set(proj).mul(view);

		this.shadowFBO.bind(this.window, true, false);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		final Program program = this.shadowProgram.getSource();
		program.bind();
		{
			while (instances.hasNext())
			{
				final Renderable inst = instances.next();
				if (!inst.isVisible()) continue;

				final Model model = inst.getModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				if (!material.hasComponent(PropertyShadow.class)) continue;
				if (!material.getComponent(PropertyShadow.class).castShadow) continue;

				Texture texture = null;
				Sprite sprite = null;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture prop = material.getComponent(PropertyTexture.class);
					texture = prop.getTexture().getSource();
					sprite = prop.getSprite();
					program.setUniform("u_transparency", prop.transparent);
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelView = view.mul(transformation, this.modelViewMatrix);

				program.setUniform("u_model_view_projection", proj.mul(modelView, this.modelViewProjMatrix));

				mesh.bind();
				{
					if (texture != null)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.bind();
						program.setUniform("u_sampler", 0);
					}

					if (sprite != null)
					{
						program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
						program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));
					}

					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

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

		this.shadowFBO.unbind(this.window, true, false);
	}

	public Matrix4f getToShadowMapSpaceMatrix()
	{
		Matrix4f mat = new Matrix4f();
		return this.offset.mul(this.lightViewProjMatrix, mat);
	}

	public FBO getShadowFBO()
	{
		return this.shadowFBO;
	}

	public Asset<Texture> getShadowMap()
	{
		return this.shadowMap;
	}

	private Matrix4f updateLightView(Vector3f dir, Vector3f center, Matrix4f dst)
	{
		dir.normalize();
		center.negate();
		dst.identity();
		float pitch = (float) Math.acos(new Vector2f(dir.x, dir.z).length());
		dst.rotate(pitch, new Vector3f(1, 0, 0));
		float yaw = (float) Math.toDegrees(((float) Math.atan(dir.x / dir.z)));
		yaw = dir.z > 0 ? yaw - 180 : yaw;
		dst.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0));
		dst.translate(center);
		return dst;
	}

	private Matrix4f updateOrthoProjection(float width, float height, float length, Matrix4f dst)
	{
		dst.identity();
		dst.m00(2F / width);
		dst.m11(2F / height);
		dst.m22(-2F / length);
		dst.m33(1F);
		return dst;
	}
}