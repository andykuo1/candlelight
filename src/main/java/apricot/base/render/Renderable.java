package apricot.base.render;

import apricot.base.render.model.Model;

import org.joml.Matrix4f;

/**
 * Created by Andy on 8/4/17.
 */
public interface Renderable
{
	default Matrix4f getRenderTransformation(Matrix4f dst)
	{
		return this.getRenderOffsetTransformation(dst).mul(this.getRenderModel().transformation());
	}

	Matrix4f getRenderOffsetTransformation(Matrix4f dst);
	Model getRenderModel();

	default boolean isRenderVisible()
	{
		return true;
	}
}
