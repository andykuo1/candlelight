package net.jimboi.apricot.base.renderer;

import net.jimboi.apricot.base.material.OldMaterial;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.render.OldRenderable;
import net.jimboi.apricot.base.renderer.property.OldPropertyDiffuse;
import net.jimboi.boron.base.render.OldRenderEngine;
import net.jimboi.boron.base.render.OldishRenderService;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;

import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class WireframeRenderer extends OldishRenderService
{
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public WireframeRenderer(OldRenderEngine renderEngine, Asset<Program> program)
	{
		super(renderEngine);
		this.program = program;
	}

	@Override
	protected void onServiceStart(OldRenderEngine handler)
	{
	}

	@Override
	protected void onServiceStop(OldRenderEngine handler)
	{
	}

	@Override
	protected void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{

	}

	public void render(Camera camera, Iterator<OldRenderable> iterator)
	{
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final OldRenderable inst = iterator.next();
				if (!inst.isRenderVisible()) continue;

				final OldModel model = inst.getRenderModel();
				final Mesh mesh = model.getMesh().getSource();
				final OldMaterial material = model.getMaterial();

				if (material.hasComponent(OldPropertyDiffuse.class))
				{
					OldPropertyDiffuse propDiffuse = material.getComponent(OldPropertyDiffuse.class);
					Vector3f diffuseColor;
					diffuseColor = new Vector3f();
					diffuseColor.x = propDiffuse.diffuseColor.x;
					diffuseColor.y = propDiffuse.diffuseColor.y;
					diffuseColor.z = propDiffuse.diffuseColor.z;
					program.setUniform("u_color", diffuseColor);
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					GL11.glDrawElements(GL11.GL_LINE_LOOP, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				mesh.unbind();
			}
		}
		program.unbind();
	}
}
