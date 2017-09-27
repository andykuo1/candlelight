package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Andy on 9/25/17.
 */
public class ThinkMachine
{
	protected final Simon user;

	public ThinkMachine(Simon user)
	{
		this.user = user;
	}

	public ActionEvent think(Console out)
	{
		EvaluationSheet eval = this.getEvaluationSheet();
		Queue<ActionEvent> events = new LinkedList<>();
		Collections.addAll(events, eval.getEvents());

		while(!events.isEmpty())
		{
			ActionEvent event = events.poll();
			boolean flag = event.evaluate(out, this.user, eval);
			if (!flag) events.offer(event);
		}

		return eval.getResult();
	}

	protected Collection<ActionEvent> getPossibleActionSet(Simon user, Collection<ActionEvent> dst)
	{
		//Get all actions possible based off of actor knowledge of environment

		return dst;
	}

	protected EvaluationSheet getEvaluationSheet()
	{
		Set<ActionEvent> events = new HashSet<>();

		//Update events with all possible actions
		this.getPossibleActionSet(this.user, events);

		int i = 0;
		ActionEvent[] dst = new ActionEvent[events.size()];
		for(ActionEvent event : events)
		{
			dst[i] = event;
		}
		return new EvaluationSheet(dst);
	}
}
