package org.bstone.application.game;

import org.bstone.application.handler.RenderHandler;
import org.bstone.application.handler.TickHandler;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 10/17/17.
 */
public interface Game extends TickHandler, RenderHandler
{
	@Override
	void onFirstUpdate();

	@Override
	default void onRenderUpdate(double delta)
	{

	}

	@Override
	default void onFixedUpdate()
	{

	}

	default ResourceLocation getAssetLocation()
	{
		return null;
	}
}
