package canary.test.gumshoe.test.opportunity.inspection.memory;

import canary.test.gumshoe.test.opportunity.inspection.query.QueryDatabase;
import canary.test.sleuth.data.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 12/20/17.
 */
public class MemoryDatabase extends QueryDatabase<Memory>
{
	public List<Memory> query(Time time)
	{
		List<Memory> dst = new ArrayList<>();
		for(Memory memory : this.getElements())
		{
			if (time.getHour() == memory.getTime().getHour())
			{
				dst.add(memory);
			}
		}
		return dst;
	}
}
