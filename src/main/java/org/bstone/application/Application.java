package org.bstone.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main entry point for any windowed application.
 */
public class Application implements Runnable
{
	private Framework framework;
	private Thread thread;

	private List<Engine> engines = new LinkedList<>();
	private Queue<Engine> cached = new LinkedBlockingQueue<>();
	private int currentIndex = -1;

	private volatile boolean running = false;

	/**
	 * Prepares the application with the client-defined framework
	 *
	 * @param framework the listening framework
	 */
	public Application setFramework(Framework framework)
	{
		if (this.running) throw new IllegalStateException("cannot change framework while running");

		this.framework = framework;
		return this;
	}

	/**
	 * Initialize the engine with lower priority than all other engines already
	 * processed in this loop. Engines that need higher order priority should be
	 * called earlier.
	 *
	 * <p> If not called on the application thread, then will instead add to the
	 * initialization queue with the lowest possible priority and be initialized
	 * at the start of the next loop on the application thread.
	 *
	 * @param engine the engine to initialize
	 *
	 * @return allows method chaining
	 */
	public Application startEngine(Engine engine)
	{
		if (engine.isDead()) throw new IllegalStateException("cannot initialize invalid engine");
		if (this.engines.contains(engine)) throw new IllegalArgumentException("engine already registered");
		for(Engine e : this.engines)
		{
			if (engine.getClass().equals(e.getClass()))
			{
				throw new IllegalArgumentException("cannot initialize engine of similar type");
			}
		}

		if (Thread.currentThread() == this.thread)
		{
			if (engine.onStart(this))
			{
				int i = this.currentIndex;
				if (i == -1) i = this.engines.size() - 1;
				this.engines.add(i + 1, engine);
			}
		}
		else
		{
			this.cached.offer(engine);
		}
		return this;
	}

	/**
	 * Mark engine for termination by the application thread.
	 *
	 * @param engine the engine to remove
	 *
	 * @return allows method chaining
	 */
	public Application stopEngine(Engine engine)
	{
		if (!this.engines.contains(engine)) throw new IllegalArgumentException("cannot remove unknown engine");

		engine.setDead();
		return this;
	}

	@Override
	public void run()
	{
		this.thread = Thread.currentThread();
		this.running = true;
		this.currentIndex = -1;

		if (this.framework == null)
		{
			throw new IllegalStateException("cannot start application without a framework - must set framework before running");
		}

		//Create application
		try
		{
			this.framework.onApplicationCreate(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

		//Start application
		try
		{
			this.framework.onApplicationStart(this);

			//Main loop
			while (this.running)
			{
				//Initialization
				while (!this.cached.isEmpty())
				{
					this.startEngine(this.cached.poll());
				}

				this.framework.onApplicationUpdate(this);

				//Update
				for (this.currentIndex = 0; this.currentIndex < this.engines.size(); ++this.currentIndex)
				{
					Engine engine = this.engines.get(this.currentIndex);

					if (engine.isDead())
					{
						engine.onStop(this);
						this.engines.remove(this.currentIndex--);
					}
					else
					{
						engine.onUpdate(this);
					}
				}
				this.currentIndex = -1;
			}

			//Stop application
			for (int i = this.engines.size() - 1; i >= 0; --i)
			{
				Engine engine = this.engines.get(i);
				engine.setDead();
				engine.onStop(this);
			}
			this.engines.clear();

			this.framework.onApplicationStop(this);
		}
		finally
		{
			//Destroy application
			this.framework.onApplicationDestroy(this);
		}
	}

	/**
	 * Stop the application from running next loop (will continue to finish current loop).
	 */
	public void stop()
	{
		this.running = false;
	}

	/**
	 * Get the current engine being processed.
	 */
	public Engine getCurrentEngine()
	{
		if (this.currentIndex == -1) throw new IllegalStateException("application not yet initialized!");

		return this.engines.get(this.currentIndex);
	}

	@SuppressWarnings("unchecked")
	public <T extends Engine> T getEngineByClass(Class<T> engineType)
	{
		for(Engine engine : this.engines)
		{
			if (engineType.isAssignableFrom(engine.getClass()))
			{
				return (T) engine;
			}
		}

		return null;
	}

	public Thread getApplicationThread()
	{
		return this.thread;
	}

	public Framework getFramework()
	{
		return this.framework;
	}
}
