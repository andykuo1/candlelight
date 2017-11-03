package org.bstone.service;

import org.bstone.RefCountSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Andy on 11/2/17.
 */
public class ServiceManager<T extends Service>
{
	public static final Set<Service> SERVICES = new RefCountSet<>("Service");

	private final Queue<ServiceEvent> events = new LinkedList<>();

	private final Queue<ServiceEntity> services = new LinkedList<>();
	private final Consumer<T> createCallback;

	private volatile boolean active = false;

	public ServiceManager()
	{
		this(null);
	}

	public ServiceManager(Consumer<T> createCallback)
	{
		this.createCallback = createCallback;
	}

	public synchronized void startService(String id, Class<? extends T> service)
	{
		this.startService(id, service, null);
	}

	public synchronized void startService(String id, Class<? extends T> service, Consumer<T> callback)
	{
		if (this.active)
		{
			for(ServiceEntity entity : this.services)
			{
				if (id.equals(entity.id))
				{
					throw new IllegalArgumentException("service with id '" + id + "' already started");
				}
			}

			T inst;
			try
			{
				inst = service.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				throw new IllegalArgumentException("could not instantiate service '" + service.getName() + "' - must have a default constructor");
			}

			if (this.createCallback != null)
			{
				this.createCallback.accept(inst);
			}

			inst.start();

			this.services.add(new ServiceEntity(id, inst));

			if (callback != null)
			{
				callback.accept(inst);
			}

			SERVICES.add(inst);
		}
		else
		{
			this.events.add(new ServiceEvent(id, service, ServiceEventType.START, callback));
		}
	}

	public synchronized void stopService(String id)
	{
		this.stopService(id, null);
	}

	public synchronized void stopService(String id, Consumer<T> callback)
	{
		if (this.active)
		{
			Iterator<ServiceEntity> iter = this.services.iterator();
			while(iter.hasNext())
			{
				ServiceEntity entity = iter.next();
				if (id.equals(entity.id))
				{
					T service = entity.service;
					service.stop();
					iter.remove();

					if (callback != null)
					{
						callback.accept(service);
					}

					SERVICES.remove(service);

					return;
				}
			}

			throw new IllegalArgumentException("cannot stop service - unable find service with id '" + id + "'");
		}
		else
		{
			this.events.add(new ServiceEvent(id, null, ServiceEventType.STOP, callback));
		}
	}

	public synchronized void pauseService(String id)
	{
		if (this.active)
		{
			for (ServiceEntity entity : this.services)
			{
				if (id.equals(entity.id))
				{
					entity.paused = true;
					return;
				}
			}

			throw new IllegalArgumentException("cannot pause service - unable find service with id '" + id + "'");
		}
		else
		{
			this.events.add(new ServiceEvent(id, null, ServiceEventType.PAUSE, null));
		}
	}

	public synchronized void resumeService(String id)
	{
		if (this.active)
		{
			for (ServiceEntity entity : this.services)
			{
				if (id.equals(entity.id))
				{
					entity.paused = false;
					return;
				}
			}

			throw new IllegalArgumentException("cannot resume service - unable find service with id '" + id + "'");
		}
		else
		{
			this.events.add(new ServiceEvent(id, null, ServiceEventType.RESUME, null));
		}
	}

	public synchronized void forEach(Consumer<T> action)
	{
		for (ServiceEntity entity : this.services)
		{
			if (!entity.paused)
			{
				action.accept(entity.service);
			}
		}
	}

	public void beginServices()
	{
		this.active = true;

		Iterator<ServiceEvent> iter = this.events.iterator();
		while(iter.hasNext())
		{
			ServiceEvent event = iter.next();
			switch (event.eventType)
			{
				case START:
					this.startService(event.id, event.service, event.callback);
					break;
				case PAUSE:
					this.pauseService(event.id);
					break;
				case RESUME:
					this.resumeService(event.id);
					break;
				case STOP:
					this.stopService(event.id, event.callback);
					break;
				default:
					throw new UnsupportedOperationException("unknown service event '" + event + "'");
			}
			iter.remove();
		}
	}

	public void endServices()
	{
		this.active = false;
	}

	public void destroy()
	{
		this.active = true;
		this.events.clear();

		Iterator<ServiceEntity> iter = this.services.iterator();
		while(iter.hasNext())
		{
			T service = iter.next().service;
			service.stop();
			iter.remove();
		}

		this.active = false;
	}

	final class ServiceEntity
	{
		final String id;
		final T service;

		boolean paused;

		ServiceEntity(String id, T service)
		{
			this.id = id;
			this.service = service;
		}
	}

	final class ServiceEvent
	{
		final String id;
		final Class<? extends T> service;
		final ServiceEventType eventType;
		final Consumer<T> callback;

		ServiceEvent(String id, Class<? extends T> service, ServiceEventType eventType, Consumer<T> callback)
		{
			this.id = id;
			this.service = service;
			this.eventType = eventType;
			this.callback = callback;
		}
	}

	enum ServiceEventType
	{
		START,
		PAUSE,
		RESUME,
		STOP;
	}
}
