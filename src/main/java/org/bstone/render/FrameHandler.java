package org.bstone.render;

/**
 * Listener for frame updates; used by {@link org.bstone.tick.TickEngine} to
 * tell {@link org.bstone.render.RenderEngine} and decide whether the frame
 * needs to be redrawn.
 */
public interface FrameHandler
{
	/**
	 * Whether the current frame should continue to be rendered. This is checked
	 * each frame.
	 */
	default boolean shouldRenderCurrentFrame()
	{
		return true;
	}

	/**
	 * The elapsed time between frame draw calls.
	 */
	default double getElapsedFrameTime()
	{
		return 1;
	}

	/**
	 * Called when the frame is redrawn.
	 */
	default void onFrameRendered()
	{}
}
