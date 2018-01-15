package net.jimboi.test.gumshoe.test.opportunity.inspection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 12/21/17.
 */
public class ItemContainer
{
	private List<String> items = new ArrayList<>();

	public void addItem(String item)
	{
		this.items.add(item);
	}

	public boolean removeItem(String item)
	{
		return this.items.remove(item);
	}

	public boolean moveItem(String item, ItemContainer dst)
	{
		if (this.removeItem(item))
		{
			dst.addItem(item);
			return true;
		}
		return false;
	}

	public boolean containsItem(String item)
	{
		return this.items.contains(item);
	}

	public Iterable<String> getItems()
	{
		return this.items;
	}
}
