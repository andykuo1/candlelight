package net.jimboi.stage_b.glim.shadow;

import net.jimboi.stage_b.glim.GlimLight;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyShadow;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.material.Material;
import org.bstone.mogli.FBO;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.model.Model;

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
	private Program program;

	private PerspectiveCamera camera;
	private Window window;

	public ShadowRenderer(PerspectiveCamera camera, Window window)
	{
		this.window = window;
		this.camera = camera;
	}

	public void load()
	{
		this.shadowBox = new ShadowBox(this.lightViewMatrix, camera, window);
		this.shadowFBO = new FBO(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE, false);

		Shader vs = new Shader(new ResourceLocation("glim:shadow.vsh"), GL20.GL_VERTEX_SHADER);
		Shader fs = new Shader(new ResourceLocation("glim:shadow.fsh"), GL20.GL_FRAGMENT_SHADER);
		this.program = new Program().link(vs, fs);
	}

	public void unload()
	{
		this.shadowFBO.close();
		this.program.close();
	}

	public void render(Iterator<Instance> instances, GlimLight light)
	{
		this.shadowBox.update();

		updateOrthoProjection(shadowBox.getWidth(), shadowBox.getHeight(), shadowBox.getLength());
		Vector3f dir = new Vector3f(light.position.x, light.position.y, light.position.z).mul(-1);
		updateLightView(dir, shadowBox.getCenter());
		this.projectionMatrix.mul(this.lightViewMatrix, this.projViewMatrix);

		this.shadowFBO.bind(this.window);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		this.program.bind();
		{
			while (instances.hasNext())
			{
				Instance inst = instances.next();
				Material mat = inst.getMaterial().getSource();
				Model model = inst.getModel().getSource();
				if (!mat.hasComponent(PropertyShadow.class)) continue;
				if (!mat.getComponent(PropertyShadow.class).castShadow) continue;

				Texture texture = null;

				if (mat.hasComponent(PropertyTexture.class))
				{
					PropertyTexture prop = mat.getComponent(PropertyTexture.class);
					texture = prop.texture;
				}

				Matrix4f matrix = new Matrix4f();
				inst.getRenderTransformation(matrix);
				model.bind();
				if (texture != null)
				{
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					texture.bind();
				}

				//TODO: disable culling if transparent . . .

				program.setUniform("u_model_view_projection", projViewMatrix.mul(matrix, matrix));
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

				if (texture != null)
				{
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					texture.unbind();
				}

				model.unbind();
			}
		}
		this.program.unbind();

		this.shadowFBO.unbind(this.window);
	}

	public Matrix4f getToShadowMapSpaceMatrix()
	{
		return this.offset.mul(this.projViewMatrix, new Matrix4f());
	}

	public FBO getShadowFBO()
	{
		return this.shadowFBO;
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