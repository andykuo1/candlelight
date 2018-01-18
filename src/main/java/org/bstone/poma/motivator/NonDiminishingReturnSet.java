package org.bstone.poma.motivator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Andy on 1/18/18.
 */
public class NonDiminishingReturnSet
{
	private final List<Element> opened = new LinkedList<>();
	private final Queue<Element> closed = new LinkedList<>();
	private int elementCount;

	public NonDiminishingReturnSet add(String s)
	{
		this.opened.add(new SimpleElement(s));
		this.elementCount++;
		return this;
	}

	public NonDiminishingReturnSet add(Supplier<String> s)
	{
		this.opened.add(new AdvancedElement(s));
		this.elementCount++;
		return this;
	}

	public String get(Random r)
	{
		int j = 100;
		String res = null;
		Element e = null;
		while(res == null)
		{
			if (e != null) this.opened.add(e);
			int i = r.nextInt(this.opened.size());
			e = this.opened.remove(i);
			res = e.getValue();
			if (--j <= 0)
			{
				this.opened.add(e);
				return "I got nothing.";
			}
		}
		this.closed.add(e);

		if (this.opened.isEmpty())
		{
			this.opened.addAll(this.closed);
			this.closed.clear();
		}

		while (!this.closed.isEmpty() && r.nextFloat() < 0.2)
		{
			this.opened.add(this.closed.poll());
		}

		return res;
	}

	interface Element
	{
		String getValue();
	}

	class AdvancedElement implements Element
	{
		final Supplier<String> value;

		AdvancedElement(Supplier<String> value)
		{
			this.value = value;
		}

		@Override
		public String getValue()
		{
			return this.value.get();
		}
	}

	class SimpleElement implements Element
	{
		final String value;

		SimpleElement(String value)
		{
			this.value = value;
		}

		@Override
		public String getValue()
		{
			return this.value;
		}
	}
}
