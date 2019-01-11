package canary.test.gumshoe.test.opportunity.inspection.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/8/17.
 */
public class QueryDatabase<E extends QueryElement>
{
	private final List<E> elements = new LinkedList<>();

	public void offer(E e)
	{
		for(int i = 0; i < this.elements.size(); ++i)
		{
			E o = this.elements.get(i);
			if (e.compareTo(o) > 0)
			{
				this.elements.add(i, e);
				return;
			}
		}

		this.elements.add(e);
	}

	public boolean remove(E e)
	{
		return this.elements.remove(e);
	}

	public List<E> query(QueryString q)
	{
		List<E> dst = new ArrayList<>();
		for(E e : this.elements)
		{
			if (q.evaluate(e))
			{
				dst.add(e);
			}
		}
		return dst;
	}

	public E queryFirst(QueryString q)
	{
		for(E e : this.elements)
		{
			if (q.evaluate(e))
			{
				return e;
			}
		}

		return null;
	}

	public Iterable<E> getElements()
	{
		return this.elements;
	}
}
