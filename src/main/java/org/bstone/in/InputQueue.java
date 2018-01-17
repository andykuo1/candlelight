package org.bstone.in;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andy on 1/17/18.
 */
public class InputQueue
{
	private Queue<InputEvent> events = new LinkedList<>();

	public void offer(InputEvent event)
	{
		this.events.offer(event);
	}

	public boolean hasNext()
	{
		return true;
	}

	public InputEvent next()
	{
		return null;
	}
}
