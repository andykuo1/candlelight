package org.bstone.application.service;

import org.bstone.service.ServiceManager;
import org.bstone.tick.TickEngine;
import org.bstone.tick.Tickable;

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
