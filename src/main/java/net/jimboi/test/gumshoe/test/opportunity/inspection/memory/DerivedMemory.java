package net.jimboi.test.gumshoe.test.opportunity.inspection.memory;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/22/17.
 */
public class DerivedMemory extends SensoryMemory
{
	private final Actor source;

	public DerivedMemory(Time time, Actor source, Actor subject, String content)
	{
		super(time, Sensory.SOCIAL, subject, content);

		this.source = source;
	}

	public Actor getSource()
	{
		return this.source;
	}
}
