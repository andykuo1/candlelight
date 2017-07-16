package org.bstone.service;

import org.bstone.RefCountSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class ServiceManager<T>
{
	private Set<Service<T>> services = new RefCountSet<>();

	private Set<Service<T>> createCache = new HashSet<>();
	private Set<Service<T>> destroyCache = new HashSet<>();

	private boolean cached = false;

	public synchronized <S extends Service<T>> S startService(S service)
	{
		if (this.cached)
		{
			if (this.destroyCache.contains(service))
			{
				this.destroyCache.remove(service);
			}
			else
			{
				this.createCache.add(service);
			}
		}
		else
		{
			this.doStartService(service);
		}
		return service;
	}

	public synchronized <S extends Service<T>> S stopService(S service)
	{
		if (this.cached)
		{
			if (this.createCache.contains(service))
			{
				this.createCache.remove(service);
			}
			else
			{
				this.destroyCache.add(service);
			}
		}
		else
		{
			this.doStopService(service);
		}
		return service;
	}

	public synchronized int countServices()
	{
		return this.services.size();
	}

	public synchronized void clearServices()
	{
		boolean prevCached = this.cached;

		this.cached = true;
		for(Service<T> service : this.services)
		{
			this.stopService(service);
		}
		this.cached = false;
		this.createCache.clear();

		for(Service<T> service : this.destroyCache)
		{
			this.doStopService(service);
		}
		this.destroyCache.clear();

		this.cached = prevCached;
	}

	protected abstract T getServiceHandler();

	protected synchronized void beginServiceBlock()
	{
		this.cached = false;
		this.flush();
	}

	protected synchronized void endServiceBlock()
	{
		this.flush();
		this.cached = true;
	}

	private void doStartService(Service<T> service)
	{
		this.services.add(service);
		service.onStart(this.getServiceHandler());
	}

	private void doStopService(Service<T> service)
	{
		this.services.remove(service);
		service.onStop(this.getServiceHandler());
	}

	private void flush()
	{
		if (!this.destroyCache.isEmpty())
		{
			for (Service<T> service : this.destroyCache)
			{
				this.doStopService(service);
			}
			this.destroyCache.clear();
		}

		if (!this.createCache.isEmpty())
		{
			for (Service<T> service : this.createCache)
			{
				this.doStartService(service);
			}
			this.createCache.clear();
		}
	}
}
