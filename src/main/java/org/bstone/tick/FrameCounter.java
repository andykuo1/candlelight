package org.bstone.tick;

/**
 * Created by Andy on 1/15/18.
 */
public class FrameCounter
{
	private final TickCounter updateCounter;
	private final TickCounter frameCounter;
	private long timeCounter = 0;

	public FrameCounter()
	{
		this.updateCounter = new TickCounter();
		this.frameCounter = new TickCounter();

		this.timeCounter = System.currentTimeMillis();
		this.updateCounter.reset();
		this.frameCounter.reset();
	}

	public void poll()
	{
		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;

			System.out.print("[");
			{
				System.out.print("UPS: " + this.updateCounter.get());
				System.out.print(" || ");
				System.out.print("FPS: " + this.frameCounter.get());
			}
			System.out.println("]");
		}
	}

	public void tick()
	{
		this.updateCounter.tick();
	}

	public void frame()
	{
		this.frameCounter.tick();
	}

	public final TickCounter getUpdateCounter()
	{
		return this.updateCounter;
	}

	public final TickCounter getFrameCounter()
	{
		return this.frameCounter;
	}
}
