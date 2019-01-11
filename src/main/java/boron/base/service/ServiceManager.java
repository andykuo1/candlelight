package boron.base.service;

import boron.bstone.RefCountSet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Andy on 7/15/17.
 */
public final class ServiceManager<S extends Service<H>, H>
{
	private Set<S> services = new RefCountSet<>("Service");

	private Set<S> createCache = new HashSet<>();
	private Set<S> destroyCache = new HashSet<>();

	private boolean cached = false;

	private final H handler;

	public ServiceManager(H handler)
	{
		this.handler = handler;
	}

	public final synchronized void forEach(Consumer<S> action)
	{
		boolean prevCached = this.cached;

		this.cached = true;
		this.services.forEach(action);
		this.cached = prevCached;
	}

	public final synchronized int countServices()
	{
		return this.services.size();
	}

	public final synchronized void clearServices()
	{
		boolean prevCached = this.cached;

		this.cached = true;
		for(S service : this.services)
		{
			this.stopService(service);
		}
		this.cached = false;

		this.createCache.clear();
		for(S service : this.destroyCache)
		{
			this.doStopService(service);
		}
		this.destroyCache.clear();

		this.cached = prevCached;
	}

	public final synchronized void beginServiceBlock()
	{
		this.cached = false;
		this.flush();
	}

	public final synchronized void endServiceBlock()
	{
		this.flush();
		this.cached = true;
	}

	public final synchronized <T extends S> T startService(T service)
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

	public final synchronized <T extends S> T stopService(T service)
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

	private void flush()
	{
		boolean flag = this.cached;
		this.cached = false;

		if (!this.destroyCache.isEmpty())
		{
			for (S service : this.destroyCache)
			{
				this.doStopService(service);
			}
			this.destroyCache.clear();
		}

		if (!this.createCache.isEmpty())
		{
			for (S service : this.createCache)
			{
				this.doStartService(service);
			}
			this.createCache.clear();
		}

		this.cached = flag;
	}

	private void doStartService(S service)
	{
		this.services.add(service);
		service.serviceManager = this;
		service.doStart(this.handler);
	}

	private void doStopService(S service)
	{
		service.doStop(this.handler);
		service.serviceManager = null;
		this.services.remove(service);
	}

	public H getServiceHandler()
	{
		return this.handler;
	}
}
