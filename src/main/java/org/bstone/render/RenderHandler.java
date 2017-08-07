package org.bstone.render;

/**
 * Created by Andy on 8/4/17.
 */
public interface RenderHandler
{
	void onRenderLoad(RenderEngine renderEngine);
	void onRenderUnload(RenderEngine renderEngine);
	void onRenderUpdate(RenderEngine renderEngine, double delta);
}
