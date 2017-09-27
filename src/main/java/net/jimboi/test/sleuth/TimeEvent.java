package net.jimboi.test.sleuth;

/**
 * Created by Andy on 9/23/17.
 */
public class TimeEvent implements Comparable<TimeEvent>
{
	private final String activity;
	private int start;
	private int length;

	public TimeEvent(String activity, int start, int length)
	{
		this.activity = activity;
		this.start = start;
		this.length = length;
	}

	public int getStart()
	{
		return this.start;
	}

	public int getEnd()
	{
		return this.start + this.length;
	}

	public int getLength()
	{
		return this.length;
	}

	@Override
	public int compareTo(TimeEvent o)
	{
		final int diff = this.start - o.start;

		if (diff < 0 && diff + o.length > 0)
		{
			return 0;
		}
		else if (diff > 0 && diff - this.length < 0)
		{
			return 0;
		}

		return diff;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TimeEvent)
		{
			return this.compareTo((TimeEvent) obj) == 0;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.start + "-" + this.getEnd() + " = " + this.activity;
	}
}
