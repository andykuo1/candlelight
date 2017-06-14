package net.jimboi.stage_b.glim.renderer.shadow;

import net.jimboi.stage_b.glim.GlimLight;
import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.glim.resourceloader.TextureLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyShadow;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

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
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class ShadowRenderer
{
	public static final int SHADOW_MAP_SIZE = 4096;
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f projViewMatrix = new Matrix4f();
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

	public void render(Iterator<Instance> instances, GlimLight light)
	{
		this.shadowBox.update();

		updateOrthoProjection(shadowBox.getWidth(), shadowBox.getHeight(), shadowBox.getLength());
		Vector3f dir = new Vector3f(light.position.x, light.position.y, light.position.z).mul(-1);
		updateLightView(dir, shadowBox.getCenter());
		this.projectionMatrix.mul(this.lightViewMatrix, this.projViewMatrix);

		this.shadowFBO.bind(this.window, true, false);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		final Program program = this.shadowProgram.getSource();
		program.bind();
		{
			while (instances.hasNext())
			{
				final Instance inst = instances.next();
				final Model model = inst.getModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				if (!material.hasComponent(PropertyShadow.class)) continue;
				if (!material.getComponent(PropertyShadow.class).castShadow) continue;

				Texture texture = null;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture prop = material.getComponent(PropertyTexture.class);
					texture = prop.texture.getSource();
				}

				Matrix4f matrix = new Matrix4f();
				inst.getRenderTransformation(matrix);
				mesh.bind();
				if (texture != null)
				{
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					texture.bind();
				}

				//TODO: disable culling if transparent . . .

				program.setUniform("u_model_view_projection", projViewMatrix.mul(matrix, matrix));
				GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

				if (texture != null)
				{
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					texture.unbind();
				}

				mesh.unbind();
			}
		}
		program.unbind();

		this.shadowFBO.unbind(this.window, true, false);
	}

	public Matrix4f getToShadowMapSpaceMatrix()
	{
		return this.offset.mul(this.projViewMatrix, new Matrix4f());
	}

	public FBO getShadowFBO()
	{
		return this.shadowFBO;
	}

	public Asset<Texture> getShadowMap()
	{
		return this.shadowMap;
	}

	private void updateLightView(Vector3f dir, Vector3f center)
	{
		dir.normalize();
		center.negate();
		lightViewMatrix.identity();
		float pitch = (float) Math.acos(new Vector2f(dir.x, dir.z).length());
		this.lightViewMatrix.rotate(pitch, new Vector3f(1, 0, 0));
		float yaw = (float) Math.toDegrees(((float) Math.atan(dir.x / dir.z)));
		yaw = dir.z > 0 ? yaw - 180 : yaw;
		this.lightViewMatrix.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0));
		this.lightViewMatrix.translate(center);
	}

	private void updateOrthoProjection(float width, float height, float length)
	{
		this.projectionMatrix.identity();
		this.projectionMatrix.m00(2F / width);
		this.projectionMatrix.m11(2F / height);
		this.projectionMatrix.m22(-2F / length);
		this.projectionMatrix.m33(1F);
	}
}