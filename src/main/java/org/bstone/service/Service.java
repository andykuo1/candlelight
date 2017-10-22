package org.bstone.service;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class Service<H> implements Comparable<Service<H>>
{
	ServiceManager serviceManager;

	private boolean active = false;
	private int priority;

	public Service(ServiceManager serviceManager)
	{
		this.serviceManager = serviceManager;
	}

	public final Service<H> start()
	{
		if (this.active) throw new IllegalStateException("Service already started!");
		this.serviceManager.startService(this);
		return this;
	}

	public final Service<H> stop()
	{
		if (!this.active) throw new IllegalStateException("Service already stopped!");
		this.serviceManager.stopService(this);
		return this;
	}

	public final void setPriority(int priority)
	{
		this.priority = priority;
	}

	public final int getPriority()
	{
		return this.priority;
	}

	final void doStart(H handler)
	{
		this.active = true;
		this.onServiceStart(handler);
	}

	final void doStop(H handler)
	{
		this.active = false;
		this.onServiceStop(handler);
	}

	protected abstract void onServiceStart(H handler);
	protected abstract void onServiceStop(H handler);

	@Override
	public int compareTo(Service<H> o)
	{
		return this.priority - o.priority;
	}

	public final boolean isActive()
	{
		return this.active;
	}
}
