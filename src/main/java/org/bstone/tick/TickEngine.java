package org.bstone.tick;

/**
 * Created by Andy on 3/1/17.
 */
public class TickEngine implements Runnable
{
	private volatile boolean running = false;

	private boolean dirty = true;

	private final boolean limitFrameRate;
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private double timeCounter;
	private int tickCounter;
	private int frameCounter;

	private final TickHandler handler;

	public TickEngine(int ticksPerSecond, boolean limitFrameRate, TickHandler handler)
	{
		this.timeStep = 1000000000D / ticksPerSecond;
		this.limitFrameRate = limitFrameRate;
		this.handler = handler;
	}

	@Override
	public void run()
	{
		this.timeCounter = System.currentTimeMillis();
		this.tickCounter = 0;
		this.frameCounter = 0;

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		this.running = true;
		this.handler.onFirstUpdate(this);
		while(this.running)
		{
			this.update();
		}
		this.handler.onLastUpdate(this);
		this.running = false;
	}

	protected void update()
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;

		this.timePrevious = current;
		this.timeLatency += elapsed;

		this.handler.onPreUpdate();

		while(this.timeLatency >= this.timeStep)
		{
			this.handler.onFixedUpdate();
			this.timeLatency -= this.timeStep;
			this.tickCounter++;
			this.dirty = true;
		}

		if (this.dirty || !this.limitFrameRate)
		{
			this.handler.onUpdate(this.timeLatency / this.timeStep);
			this.frameCounter++;
			this.dirty = false;
		}

		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;
			System.out.println("[UPS: " + this.tickCounter + " || FPS: " + this.frameCounter + "]");
			this.tickCounter = 0;
			this.frameCounter = 0;
		}
	}

	public void stop()
	{
		this.running = false;
	}

	public boolean isRunning()
	{
		return this.running;
	}
}