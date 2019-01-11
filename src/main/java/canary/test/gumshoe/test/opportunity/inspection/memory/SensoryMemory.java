package canary.test.gumshoe.test.opportunity.inspection.memory;

import canary.test.gumshoe.test.opportunity.inspection.Actor;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import canary.test.sleuth.data.Time;

/**
 * Created by Andy on 12/8/17.
 */
public class SensoryMemory extends Memory
{
	//I ate an apple.
	//I shared an apple with Rob.
	//I talked to Rob.

	//I saw Bob eat an apple.
	//I saw Rob shared an apple with Bob.
	//I saw Bob talk to Rob.
	//I heard Bob eat an apple.
	//I heard from Rob that Bob ate an apple.

	private final Sensory input;
	private final Actor subject;

	public SensoryMemory(Time time, Sensory input, Actor subject, String content)
	{
		super(time, content);

		this.input = input;
		this.subject = subject;
	}

	public Sensory getInput()
	{
		return this.input;
	}

	public Actor getSubject()
	{
		return this.subject;
	}
}
