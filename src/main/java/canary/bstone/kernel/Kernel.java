package canary.bstone.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 1/15/18.
 */
public class Kernel implements Runnable
{
	private static final Logger LOG = LoggerFactory.getLogger(Kernel.class);

	private Thread thread;

	private List<KernelComponent> components = new LinkedList<>();
	private Queue<Engine> cached = new LinkedBlockingQueue<>();
	private int currentIndex = -1;

	private volatile boolean running = false;

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
	public Kernel startEngine(Engine engine)
	{
		KernelComponent component = new KernelComponent(engine);

		if (this.components.contains(component)) throw new IllegalArgumentException("engine already registered");

		for(KernelComponent e : this.components)
		{
			if (engine.getClass().equals(e.getEngine().getClass()))
			{
				throw new IllegalArgumentException("cannot initialize engine of same type '" + engine.getClass() + "'.");
			}
		}

		if (Thread.currentThread() == this.thread)
		{
			if (component.onStart())
			{
				int i = this.currentIndex;
				if (i == -1) i = this.components.size() - 1;
				this.components.add(i + 1, component);
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
	public Kernel stopEngine(Engine engine)
	{
		KernelComponent component = null;
		for(KernelComponent c : this.components)
		{
			if (engine.equals(c.getEngine()))
			{
				component = c;
				break;
			}
		}

		if (component == null) throw new IllegalArgumentException("cannot remove unknown engine");

		component.setDead();
		return this;
	}

	/**
	 * Start running the Kernel on the current thread
	 */
	@Override
	public void run()
	{
		this.thread = Thread.currentThread();
		this.running = true;
		this.currentIndex = -1;

		try
		{
			LOG.info("Starting kernel...");
			this.onKernelStart();

			//Main loop
			while (this.running)
			{
				//Initialization
				while (!this.cached.isEmpty())
				{
					this.startEngine(this.cached.poll());
				}

				this.onKernelUpdate();

				//Update
				for (this.currentIndex = 0; this.currentIndex < this.components.size(); ++this.currentIndex)
				{
					KernelComponent engine = this.components.get(this.currentIndex);

					if (engine.isDead())
					{
						engine.onStop();
						this.components.remove(this.currentIndex--);
					}
					else
					{
						engine.onUpdate();
					}
				}
				this.currentIndex = -1;
			}
		}
		finally
		{
			LOG.info("Stopping kernel...");
			this.onKernelStop();

			//Termination
			for (int i = this.components.size() - 1; i >= 0; --i)
			{
				KernelComponent engine = this.components.get(i);
				engine.setDead();
				engine.onStop();
			}
			this.components.clear();
		}
	}

	/**
	 * Stop the application from running next loop (will continue to finish current loop).
	 */
	public void stop()
	{
		this.running = false;
	}

	protected void onKernelStart() {}
	protected void onKernelUpdate() {}
	protected void onKernelStop() {}

	/**
	 * Get the current engine being processed.
	 */
	public Engine getCurrentEngine()
	{
		if (this.currentIndex == -1) throw new IllegalStateException("application not yet initialized!");

		return this.components.get(this.currentIndex).getEngine();
	}

	@SuppressWarnings("unchecked")
	public <T extends Engine> T getEngineByClass(Class<T> engineType)
	{
		for(KernelComponent component : this.components)
		{
			Engine engine = component.getEngine();
			if (engineType.isAssignableFrom(engine.getClass()))
			{
				return (T) engine;
			}
		}

		return null;
	}

	public Thread getKernelThread()
	{
		return this.thread;
	}

	public boolean isRunning()
	{
		return this.running;
	}

	@Override
	public String toString()
	{
		return super.toString() + ":" + this.thread.getName();
	}
}
