package org.zilar.renderer;

import org.bstone.camera.Camera;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.renderer.Drawable;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.renderer.RenderService;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;

import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class WireframeRenderer extends RenderService
{
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public WireframeRenderer(Asset<Program> program)
	{
		this.program = program;
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
	}

	public void render(Camera camera, Iterator<Drawable> iterator)
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
				final Drawable inst = iterator.next();
				if (!inst.isVisible()) continue;

				final Model model = inst.getModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				if (material.hasComponent(PropertyDiffuse.class))
				{
					PropertyDiffuse propDiffuse = material.getComponent(PropertyDiffuse.class);
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
