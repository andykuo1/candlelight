package apricot.base.render;

import apricot.base.render.model.Model;

import org.joml.Matrix4f;

/**
 * Created by Andy on 8/5/17.
 */
public class RenderableBase implements Renderable
{
	private Matrix4f transformation;
	private Model model;
	private boolean visible;

	public RenderableBase(Model model, Matrix4f transformation)
	{
		this.transformation = transformation;
		this.model = model;
		this.visible = true;
	}

	public Matrix4f transformation()
	{
		return this.transformation;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		return dst.set(this.transformation);
	}

	@Override
	public Model getRenderModel()
	{
		return this.model;
	}

	@Override
	public boolean isRenderVisible()
	{
		return this.visible;
	}
}
