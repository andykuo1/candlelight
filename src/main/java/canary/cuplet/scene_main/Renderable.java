package canary.cuplet.scene_main;

import canary.base.Model;

import org.joml.Matrix4f;

/**
 * Created by Andy on 11/1/17.
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