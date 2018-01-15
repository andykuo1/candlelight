package org.bstone.tick;

import org.bstone.service.ServiceManager;

/**
 * Created by Andy on 1/15/18.
 */
public class TickServiceManager extends ServiceManager<TickEngine, TickService> implements Tickable
{
	public TickServiceManager(TickEngine handler)
	{
		super(handler);
	}

	@Override
	public void tick()
	{
		this.beginServices();
		this.forEach(TickService::onFixedUpdate);
		this.endServices();
	}
}
