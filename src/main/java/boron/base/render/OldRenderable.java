package boron.base.render;

import org.joml.Matrix4f;

/**
 * Created by Andy on 8/4/17.
 */
@Deprecated
public interface OldRenderable
{
	default Matrix4f getRenderTransformation(Matrix4f dst)
	{
		return this.getRenderOffsetTransformation(dst).mul(this.getRenderModel().transformation());
	}

	Matrix4f getRenderOffsetTransformation(Matrix4f dst);
	OldModel getRenderModel();

	default boolean isRenderVisible()
	{
		return true;
	}
}
