package org.bstone.tick;

/**
 * Created by Andy on 3/1/17.
 */
public class TickEngine
{
	public interface Listener
	{
		void onUpdate(double delta);
		void onUpdate();
	}

	private final Listener listener;
	private volatile boolean running = false;

	private final double dt = 1 / 60D;
	private double t = 0.0;

	private double time = 0.0;

	private double newTime;
	private double frameTime;
	private double currentTime;

	public TickEngine(final Listener listener)
	{
		this.listener = listener;
		this.running = true;
	}

	public void update()
	{
		this.newTime = getCurrentTime();
		this.frameTime = this.newTime - this.currentTime;
		this.currentTime = this.newTime;

		this.time += this.frameTime;

		while (this.time >= this.dt)
		{
			this.listener.onUpdate(this.dt);

			this.time -= this.dt;
			this.t += this.dt;
		}

		this.listener.onUpdate();
	}

	public void stop()
	{
		this.running = false;
	}

	public boolean shouldKeepRunning()
	{
		return this.running;
	}

	public static double getCurrentTime()
	{
		return System.nanoTime() / 1000000000D;
	}
}
