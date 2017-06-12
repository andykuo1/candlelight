package net.jimboi.stage_b.glim.bounding;

import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.bounding.square.Circle;
import net.jimboi.stage_b.glim.resourceloader.MeshLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.meshbuilder.MeshBuilder;
import net.jimboi.stage_b.gnome.meshbuilder.ModelUtil;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

/**
 * Created by Andy on 6/11/17.
 */
public class BoundingRenderer
{
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	private Asset<Mesh> box;
	private Asset<Mesh> sphere;
	private Asset<Mesh> cylinder;
	private Vector3f color = new Vector3f(1, 1, 1);

	public BoundingRenderer(Asset<Program> program)
	{
		this.program = program;
	}

	public void setup(AssetManager assetManager)
	{
		MeshBuilder mb = new MeshBuilder();
		mb.addCircle(0, 0, 1, 6);
		Mesh mesh = ModelUtil.createMesh(mb.bake(false, false));
		mb.clear();

		//Mesh
		this.box = assetManager.getAsset(Mesh.class, "box");
		this.sphere = assetManager.registerAsset(Mesh.class, "b_circle",
				new MeshLoader.MeshParameter(mesh));
		this.cylinder = assetManager.registerAsset(Mesh.class, "b_cylinder",
				new MeshLoader.MeshParameter(new ResourceLocation("glim:cylinder.obj")));
	}

	public void render(Camera camera, Iterator<Bounding> iterator)
	{
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		Vector3f position = new Vector3f();
		Vector3f scale = new Vector3f();

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final Bounding bounding = iterator.next();
				position.set(bounding.position());
				Mesh mesh;
				if (bounding instanceof AABB)
				{
					mesh = this.sphere.getSource();
					Vector2fc radius = ((AABB) bounding).radius;
					scale.set(radius.x() * 2, 1, radius.y() * 2);
				}
				else if (bounding instanceof Circle)
				{
					mesh = this.cylinder.getSource();
					float radius = ((Circle) bounding).radius;
					scale.set(radius, radius, radius);
				}
				else
				{
					mesh = this.sphere.getSource();
					scale.set(1, 1, 1);
				}

				Matrix4fc transform = this.modelMatrix.identity().translate(position).scale(scale);
				Matrix4fc modelViewProj = projView.mul(transform, this.modelViewProjMatrix);
				program.setUniform("u_model", transform);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					program.setUniform("u_color", this.color);

					GL11.glDrawElements(GL11.GL_LINE_LOOP, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				mesh.unbind();
			}
		}
		program.unbind();
	}
}
