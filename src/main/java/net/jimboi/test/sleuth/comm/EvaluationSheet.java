package net.jimboi.test.sleuth.comm;

/**
 * Created by Andy on 9/25/17.
 */
public class EvaluationSheet
{
	private final ActionEvent[] events;
	public final int[] costs;

	protected EvaluationSheet(ActionEvent...events)
	{
		this.events = events;
		this.costs = new int[events.length];
	}

	public ActionEvent getEvent(int index)
	{
		return this.events[index];
	}

	public ActionEvent[] getEvents()
	{
		return this.events;
	}

	public ActionEvent getResult()
	{
		int j = 0;
		int ij = this.costs[j];
		int ic;
		for(int i = 1; i < this.costs.length; ++i)
		{
			ic = this.costs[i];
			if (ij < ic) continue;
			j = i;
			ij = ic;
		}
		return this.events[j];
	}

	public int indexOf(ActionEvent event)
	{
		for(int i = 0; i < this.events.length; ++i)
		{
			if (this.events[i] == event) return i;
		}

		return -1;
	}

	public int length()
	{
		return this.events.length;
	}
}
