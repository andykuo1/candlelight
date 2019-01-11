package canary.zilar.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andy on 11/28/17.
 */
public class GuiIterator implements Iterator<GuiBase>
{
	private final Queue<Iterable<GuiBase>> children = new LinkedList<>();
	private final GuiBase root;

	private Iterator<GuiBase> iter;

	public GuiIterator(GuiBase root)
	{
		this.root = root;

		if (this.root != null)
		{
			this.iter = this.root.getChildren().iterator();
		}
	}

	@Override
	public boolean hasNext()
	{
		if (this.iter == null) return false;

		while(!this.iter.hasNext())
		{
			if (this.children.isEmpty()) return false;

			this.iter = this.children.poll().iterator();
		}

		return true;
	}

	@Override
	public GuiBase next()
	{
		GuiBase child = this.iter.next();
		this.children.add(child.getChildren());
		return child;
	}
}
