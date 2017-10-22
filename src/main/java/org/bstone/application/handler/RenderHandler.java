package org.bstone.application.handler;

/**
 * Created by Andy on 10/12/17.
 */
public interface RenderHandler
{
	default void onRenderLoad() {}
	void onRenderUpdate(double delta);
	default void onRenderUnload() {}
}
