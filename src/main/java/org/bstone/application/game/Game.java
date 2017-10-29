package org.bstone.application.game;

import org.bstone.application.handler.RenderHandler;
import org.bstone.application.handler.TickHandler;
import org.bstone.input.InputListener;
import org.bstone.input.context.InputContext;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 10/17/17.
 */
public interface Game extends TickHandler, RenderHandler, InputListener
{
	@Override
	void onFirstUpdate();

	@Override
	default void onRenderUpdate(double delta)
	{

	}

	@Override
	default void onInputUpdate(InputContext context)
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
