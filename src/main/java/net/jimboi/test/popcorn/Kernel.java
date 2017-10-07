package net.jimboi.test.popcorn;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Andy on 10/6/17.
 */
public class Kernel implements Runnable
{
	private final Queue<Engine> engines = new PriorityBlockingQueue<>(11,
			Comparator.comparingInt(Engine::getPriority));
	private final Set<Engine> cached = new HashSet<>();

	private boolean dirty = false;
	private final Set<Engine> resumed = new HashSet<>();
	private final Set<Engine> suspended = new HashSet<>();

	@Override
	public void run()
	{
		this.engines.addAll(this.cached);
		this.cached.clear();

		while(!this.engines.isEmpty() && !this.suspended.isEmpty())
		{
			Iterator<Engine> iter = this.engines.iterator();
			while (iter.hasNext())
			{
				final Engine engine = iter.next();
				if (engine.isDead())
				{
					engine.onStop();
					iter.remove();
				}
				else
				{
					engine.onUpdate();
				}
			}

			if (this.dirty)
			{
				this.engines.removeAll(this.suspended);

				if (!this.resumed.isEmpty())
				{
					this.engines.addAll(this.resumed);
					this.resumed.clear();
				}

				if (!this.cached.isEmpty())
				{
					this.engines.addAll(this.cached);
					this.cached.clear();
				}

				this.dirty = false;
			}
		}
	}

	public boolean startEngine(Engine engine)
	{
		if (!engine.onStart()) return false;
		this.cached.add(engine);
		this.dirty = true;
		return true;
	}

	public void stopEngine(Engine engine)
	{
		engine.setDead();
	}

	public void suspendEngine(Engine engine)
	{
		if (!this.engines.contains(engine)) return;

		if (this.resumed.remove(engine) || this.suspended.add(engine))
		{
			engine.onSuspend();
			this.dirty = true;
		}
	}

	public void resumeEngine(Engine engine)
	{
		if (!this.engines.contains(engine)) return;

		if (this.suspended.remove(engine) || this.resumed.add(engine))
		{
			engine.onResume();
			this.dirty = true;
		}
	}
}
