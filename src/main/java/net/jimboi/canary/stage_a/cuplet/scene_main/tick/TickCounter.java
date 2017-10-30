package net.jimboi.canary.stage_a.cuplet.scene_main.tick;

/**
 * Created by Andy on 8/13/17.
 */
public final class TickCounter
{
	private final int maxTicks;
	private int ticks;

	public TickCounter(int maxTicks)
	{
		this(maxTicks, false);
	}

	public TickCounter(int maxTicks, boolean isFull)
	{
		this.maxTicks = maxTicks;
		this.ticks = isFull ? this.maxTicks : 0;
	}

	public TickCounter setTicks(int ticks)
	{
		this.ticks = ticks;
		return this;
	}

	public int tick()
	{
		return ++this.ticks;
	}

	public void reset()
	{
		this.ticks = 0;
	}

	public void resetWithBuffer(int buffer)
	{
		this.ticks = -buffer;
	}

	public void setComplete()
	{
		this.ticks = this.maxTicks;
	}

	public boolean isComplete()
	{
		return this.ticks >= this.maxTicks;
	}

	public boolean isBuffering()
	{
		return this.ticks < 0;
	}

	public boolean hasTicks()
	{
		return this.ticks > 0;
	}

	public float getProgress()
	{
		return this.ticks / (float) this.maxTicks;
	}

	public int getTicks()
	{
		return this.ticks;
	}

	public int getMaxTicks()
	{
		return this.maxTicks;
	}
}
