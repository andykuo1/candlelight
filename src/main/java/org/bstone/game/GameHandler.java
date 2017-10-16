package org.bstone.game;

import net.jimboi.boron.base.render.OldRenderEngine;

/**
 * Created by Andy on 8/5/17.
 */
public interface GameHandler
{
	void onFirstUpdate();
	void onPreUpdate();
	void onUpdate();
	void onLastUpdate();

	void onLoad(OldRenderEngine renderEngine);
	void onRender(OldRenderEngine renderEngine, double delta);
	void onUnload(OldRenderEngine renderEngine);

	default boolean onWindowClose()
	{
		return true;
	}
}
