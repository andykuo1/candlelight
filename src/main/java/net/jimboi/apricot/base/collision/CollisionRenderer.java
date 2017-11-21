package net.jimboi.apricot.base.collision;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.render.OldRenderEngine;
import net.jimboi.boron.base_ab.render.OldishRenderService;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.transform.Transform2;
import org.bstone.util.ColorUtil;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;

import java.util.Iterator;

/**
 * Created by Andy on 7/20/17.
 */
public class CollisionRenderer extends OldishRenderService
{
	private final CollisionManager collisionManager;
	private final Asset<Program> program;
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();
	private final Matrix4f modelViewProjMatrix = new Matrix4f();

	private final Vector3f color = new Vector3f(0, 1, 0);

	private Mesh QUAD;
	private Mesh CIRCLE;
	private Mesh SEGMENT;
	private Mesh POINT;

	public CollisionRenderer(OldRenderEngine renderEngine, CollisionManager collisionManager, Asset<Program> program)
	{
		super(renderEngine);
		this.collisionManager = collisionManager;
		this.program = program;
	}

	public CollisionRenderer setColor(int color)
	{
		ColorUtil.getNormalizedRGB(color, this.color);
		return this;
	}

	@Override
	protected void onServiceStart(OldRenderEngine handler)
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
	protected void onServiceStop(OldRenderEngine handler)
	{
		try
		{
			CIRCLE.close();
			QUAD.close();
			POINT.close();
			SEGMENT.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{

	}

	public Mesh getMeshForShape(Shape shape)
	{
		if (shape instanceof Shape.AABB) return QUAD;
		if (shape instanceof Shape.Circle) return CIRCLE;
		if (shape instanceof Shape.Point) return POINT;
		if (shape instanceof Shape.Segment) return SEGMENT;

		throw new UnsupportedOperationException("Unable to draw shape '" + shape.getClass() + "'!");
	}

	public Matrix4f getTransformForShape(Shape shape, Matrix4f dst)
	{
		dst.translation(shape.getCenterX(), shape.getCenterY(), 0);
		if (shape instanceof Shape.AABB)
		{
			Shape.AABB aabb = (Shape.AABB) shape;
			dst.scale(aabb.getHalfWidth() * 2, aabb.getHalfHeight() * 2, 1);
		}
		else if (shape instanceof Shape.Circle)
		{
			Shape.Circle circle = (Shape.Circle) shape;
			dst.scale(circle.getRadius() * 2, circle.getRadius() * 2, 1);
		}
		else if (shape instanceof Shape.Point)
		{
			Shape.Point point = (Shape.Point) shape;
		}
		else if (shape instanceof Shape.Segment)
		{
			Shape.Segment segment = (Shape.Segment) shape;

			Vector2fc delta = segment.getDelta(new Vector2f());
			float len = delta.length();

			dst.rotateZ(Transform2.XAXIS.angle(delta));
			dst.scale(len, len, 1);
		}
		return dst;
	}

	public void drawElementsForShape(Shape shape, Mesh mesh)
	{
		int mode = shape instanceof Shape.Point ? GL11.GL_POINTS : GL11.GL_LINE_LOOP;
		GL11.glDrawArrays(mode, 0, mesh.getVertexCount());
	}

	public void render(Camera camera, Matrix4fc offsetView)
	{
		Iterator<Shape> iterator = this.collisionManager.getShapes();
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix).mul(offsetView, this.projViewMatrix);

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
				program.setUniform("u_color", this.color);

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
