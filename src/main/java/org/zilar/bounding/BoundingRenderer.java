package org.zilar.bounding;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.transform.Transform2;
import org.bstone.window.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;

import java.util.Iterator;

/**
 * Created by Andy on 7/14/17.
 */
public class BoundingRenderer extends RenderService
{
	private final BoundingManager boundingManager;
	private final Asset<Program> program;
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();
	private final Matrix4f modelViewProjMatrix = new Matrix4f();

	private Mesh QUAD;
	private Mesh CIRCLE;
	private Mesh SEGMENT;
	private Mesh POINT;

	public BoundingRenderer(BoundingManager boundingManager, Asset<Program> program)
	{
		this.boundingManager = boundingManager;
		this.program = program;
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
		MeshBuilder mb = new MeshBuilder();

		mb.addCircle(0, 0, 0.5F, 8);
		CIRCLE = ModelUtil.createStaticMesh(mb.bake(false, false));
		mb.clear();

		mb.addQuad(0, 0, 1F, 1F);
		QUAD = ModelUtil.createStaticMesh(mb.bake(false, false));
		mb.clear();

		mb.addPoint(0, 0);
		POINT = ModelUtil.createStaticMesh(mb.bake(false, false));
		mb.clear();

		mb.addSegment(0, 0, 1, 0);
		SEGMENT = ModelUtil.createStaticMesh(mb.bake(false, false));
		mb.clear();
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
		CIRCLE.close();
		QUAD.close();
		POINT.close();
		SEGMENT.close();
	}

	public Mesh getMeshForShape(Shape shape)
	{
		if (shape instanceof ShapeAABB) return QUAD;
		if (shape instanceof ShapeCircle) return CIRCLE;
		if (shape instanceof ShapePoint) return POINT;
		if (shape instanceof ShapeSegment) return SEGMENT;

		throw new UnsupportedOperationException("Unable to draw shape '" + shape.getClass() + "'!");
	}

	public Matrix4f getTransformForShape(Shape shape, Matrix4f dst)
	{
		dst.translation(shape.center.x(), shape.center.y(), 0);
		if (shape instanceof ShapeAABB)
		{
			Vector2fc radius = ((ShapeAABB) shape).radius();
			dst.scale(radius.x() * 2, radius.y() * 2, 1);
		}
		else if (shape instanceof ShapeCircle)
		{
			float radius = ((ShapeCircle) shape).radius();
			dst.scale(radius * 2, radius * 2, 1);
		}
		else if (shape instanceof ShapePoint)
		{
		}
		else if (shape instanceof ShapeSegment)
		{
			Vector2fc delta = ((ShapeSegment) shape).delta();
			float len = delta.length();
			dst.rotateZ(Transform2.XAXIS.angle(delta));
			dst.scale(len, len, 1);
		}
		return dst;
	}

	public void drawElementsForShape(Shape shape, Mesh mesh)
	{
		int mode = shape instanceof ShapePoint ? GL11.GL_POINTS : GL11.GL_LINE_LOOP;
		GL11.glDrawArrays(mode, 0, mesh.getVertexCount());
	}

	public void render(Camera camera)
	{
		Iterator<Shape> iterator = this.boundingManager.getShapeIterator();
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final Shape shape = iterator.next();

				final Mesh mesh = this.getMeshForShape(shape);
				program.setUniform("u_color", new Vector3f(0, 1, 0));

				Matrix4fc transformation = this.getTransformForShape(shape, this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					this.drawElementsForShape(shape, mesh);
				}
				mesh.unbind();
			}
		}
		program.unbind();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
