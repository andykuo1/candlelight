package org.bstone.game;

import org.bstone.render.RenderEngine;

/**
 * Created by Andy on 8/5/17.
 */
public interface GameHandler
{
	void onFirstUpdate();
	void onPreUpdate();
	void onUpdate();
	void onLastUpdate();

	void onLoad(RenderEngine renderEngine);
	void onRender(RenderEngine renderEngine, double delta);
	void onUnload(RenderEngine renderEngine);

	default boolean onWindowClose()
	{
		return true;
	}
}
