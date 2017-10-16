package net.jimboi.test.popcorn;

import org.bstone.render.RenderEngine;

/**
 * Created by Andy on 10/12/17.
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
