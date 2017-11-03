package net.jimboi.test.sleuth.cluedo;

import net.jimboi.test.sleuth.PrintableStruct;
import net.jimboi.test.sleuth.data.Time;

import org.bstone.util.pair.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 9/23/17.
 */
public class MemoryBank extends PrintableStruct
{
	public final List<Memory> data = new LinkedList<>();

	public void memorize(Time time, String event)
	{
		this.data.add(new Memory(new Time(time), event));
	}

	private static final class Memory extends Pair<Time, String>
	{
		public Memory(Time var1, String var2)
		{
			super(var1, var2);
		}
	}
}
