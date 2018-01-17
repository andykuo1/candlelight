package org.bstone.application.service;

import org.bstone.service.Service;
import org.bstone.tick.TickEngine;

/**
 * Created by Andy on 11/4/17.
 */
public abstract class TickService implements Service<TickEngine>
{
	@Override
	public final void start(TickEngine handler)
	{
		this.onFirstUpdate(handler);
	}

	@Override
	public final void stop(TickEngine handler)
	{
		this.onLastUpdate(handler);
	}

	protected abstract void onFirstUpdate(TickEngine tickEngine);

	protected abstract void onLastUpdate(TickEngine tickEngine);

	protected abstract void onFixedUpdate();
}
