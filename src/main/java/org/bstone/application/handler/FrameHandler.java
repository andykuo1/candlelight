package org.bstone.application.handler;

/**
 * Created by Andy on 10/20/17.
 */
public interface FrameHandler
{
	default boolean shouldRenderCurrentFrame()
	{
		return true;
	}

	default double getElapsedFrameTime()
	{
		return 1;
	}

	default void onFrameRendered()
	{}
}
