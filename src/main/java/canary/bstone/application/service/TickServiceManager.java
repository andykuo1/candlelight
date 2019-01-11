package canary.bstone.application.service;

import canary.bstone.service.ServiceManager;
import canary.bstone.tick.TickEngine;
import canary.bstone.tick.Tickable;

/**
 * Created by Andy on 1/15/18.
 */
public class TickServiceManager extends ServiceManager<TickEngine, TickService> implements Tickable
{
	@Override
	public void start()
	{
		this.beginServices();
		this.endServices();
	}

	@Override
	public void stop()
	{
		this.beginServices();
		this.endServices();
	}

	@Override
	public void tick()
	{
		this.beginServices();
		this.forEach(TickService::onFixedUpdate);
		this.endServices();
	}
}
