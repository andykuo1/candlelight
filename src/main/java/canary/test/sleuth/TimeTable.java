package canary.test.sleuth;

import canary.test.sleuth.data.WeekDay;

import canary.bstone.util.small.SmallMap;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andy on 9/23/17.
 */
public class TimeTable
{
	public Map<WeekDay, Set<TimeEvent>> table = new SmallMap<>();

	public void add(String activity, int start, int length, WeekDay day)
	{
		Set<TimeEvent> events = this.table.computeIfAbsent(day, k -> new TreeSet<>());
		int diff = 24 - (start + length);
		if (diff < 0)
		{
			diff = -diff;
			this.add(activity, 0, diff, day.next());
			length = length - diff;
		}
		events.add(new TimeEvent(activity, start, length));
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for(WeekDay day : WeekDay.values())
		{
			Set<TimeEvent> events = this.table.get(day);
			if (events == null) continue;
			sb.append("   + ");
			sb.append(day);
			sb.append(":");
			sb.append("\n");

			for(TimeEvent event : events)
			{
				sb.append("     - ");
				sb.append(event.toString());
				sb.append("\n");
			}
		}
		return "\n" + sb.toString();
	}
}
