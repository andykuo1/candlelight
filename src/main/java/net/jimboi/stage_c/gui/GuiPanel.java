package net.jimboi.stage_c.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/12/17.
 */
public class GuiPanel extends Gui
{
	private List<Gui> childs = new ArrayList<>();

	public GuiPanel(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}

	@Override
	protected void onEnter()
	{

	}

	@Override
	protected void onLeave()
	{

	}

	@Override
	protected void onSelect()
	{

	}

	@Override
	protected void onAction()
	{

	}

	@Override
	protected void updateGui()
	{
		for(Gui child : this.childs)
		{
			child.markDirty();
		}
	}

	public final void addChild(Gui gui)
	{
		if (gui.getParent() == this) return;
		if (gui.getParent() != null) throw new IllegalArgumentException("Gui must have 'null' as parent to be added as child!");
		if (gui == this) throw new IllegalArgumentException("Gui hierarchy must not be cyclical!");

		if (!this.childs.add(gui)) throw new IllegalArgumentException("Could not add gui as child!");

		gui.setParent(this);
		gui.markDirty();
	}

	public final void removeChild(Gui gui)
	{
		if (gui.getParent() != this) throw new IllegalArgumentException("Trying to remove a gui from unrelated parent!");

		this.childs.remove(gui);

		gui.setParent(null);
		gui.markDirty();
	}

	public boolean hasChild(Gui gui)
	{
		return gui.getParent() == this;
	}
}
