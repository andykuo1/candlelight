package boron.bstone.tick;

/**
 * Created by Andy on 8/2/17.
 */
public class TickCounter
{
	protected int ticks;

	public void tick()
	{
		this.ticks++;
	}

	public int peek()
	{
		return this.ticks;
	}

	public int get()
	{
		int i = this.ticks;
		this.ticks = 0;
		return i;
	}

	public void reset()
	{
		this.ticks = 0;
	}
}
