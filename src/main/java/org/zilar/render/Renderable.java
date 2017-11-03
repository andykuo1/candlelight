package org.zilar.render;

import org.joml.Matrix4f;
import org.zilar.render.model.Model;

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
