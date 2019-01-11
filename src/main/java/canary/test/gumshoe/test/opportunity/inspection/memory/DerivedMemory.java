package canary.test.gumshoe.test.opportunity.inspection.memory;

import canary.test.gumshoe.test.opportunity.inspection.Actor;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import canary.test.sleuth.data.Time;

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
