package org.bstone.tick;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 3/1/17.
 */
public class TickEngine
{
	public interface OnFixedUpdateListener
	{
		void onUpdate(double delta);
	}

	public interface OnUpdateListener
	{
		void onUpdate();
	}

	public final Listenable<OnFixedUpdateListener> onFixedUpdate = new Listenable<>((listener, objects) -> listener.onUpdate((Double) objects[0]));
	public final Listenable<OnUpdateListener> onUpdate = new Listenable<>((listener, objects) -> listener.onUpdate());

	private volatile boolean running = false;

	private final double dt = 1 / 60D;
	private double t = 0.0;

	private double time = 0.0;

	private double newTime;
	private double frameTime;
	private double currentTime;

	public TickEngine()
	{
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
			this.onFixedUpdate.notifyListeners(this.dt);
			this.time -= this.dt;
			this.t += this.dt;
		}

		this.onUpdate.notifyListeners();
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
