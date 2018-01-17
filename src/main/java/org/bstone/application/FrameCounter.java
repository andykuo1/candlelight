package org.bstone.application;

import org.bstone.util.Counter;

/**
 * Created by Andy on 1/15/18.
 */
public class FrameCounter
{
	private final Counter updates;
	private final Counter frames;
	private long time = 0;

	public FrameCounter()
	{
		this.updates = new Counter();
		this.frames = new Counter();

		this.time = System.currentTimeMillis();
		this.updates.reset();
		this.frames.reset();
	}

	public void poll()
	{
		if (System.currentTimeMillis() - this.time > 1000)
		{
			this.time += 1000;

			System.out.print("[");
			{
				System.out.print("UPS: " + this.updates.poll());
				System.out.print(" || ");
				System.out.print("FPS: " + this.frames.poll());
			}
			System.out.println("]");
		}
	}

	public void tick()
	{
		this.updates.next();
	}

	public void frame()
	{
		this.frames.next();
	}

	public final Counter getUpdates()
	{
		return this.updates;
	}

	public final Counter getFrames()
	{
		return this.frames;
	}
}
