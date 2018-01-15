package net.jimboi.test.gumshoe.test.opportunity.inspection.memory;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.opportunity.inspection.query.QueryElement;
import net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/8/17.
 */
public class Memory extends QueryElement
{
	private final Time time;
	private final String content;

	public Memory(Time time, String content)
	{
		super(time.getTotalHours(), content);

		this.time = new Time(time);
		this.content = content;
	}

	public Time getTime()
	{
		return this.time;
	}

	public String getContent()
	{
		return this.content;
	}

	public static String toString(Sensory input, Actor source, Actor subject, String content)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("I remembered");

		if (subject != null)
		{
			sb.append(' ');
			switch (input)
			{
				case VISUAL:
					sb.append("I saw");
					break;
				case AUDIAL:
					sb.append("I heard");
					break;
				case SOCIAL:
					sb.append("I was told");
					break;
			}

			if (source != null)
			{
				sb.append(' ');
				sb.append("by ");
				sb.append(source.getName());
			}

			sb.append(' ');
			sb.append(subject.getName());
		}
		else
		{
			sb.append(' ');
			sb.append("I");
		}

		sb.append(' ');
		sb.append(content);
		return sb.toString();
	}
}
