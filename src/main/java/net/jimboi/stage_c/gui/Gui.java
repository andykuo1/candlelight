package net.jimboi.stage_c.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/15/17.
 */
public class Gui implements Comparable<Gui>
{
	private final List<Gui> childs = new ArrayList<>();
	private Gui parent;

	public int depth = 1;

	public float x;
	public float y;

	public float width = 1;
	public float height = 1;

	public int color = 0xFFFFFF;
	public float alpha = 1;

	public boolean isSelectable(float x, float y)
	{
		return contains(this, x, y) && this.alpha > 0;
	}

	public void onGuiStateChanged(int state)
	{
		switch (state)
		{
			case 1: //HOVER
				break;
			case 2: //ACTIVE
				break;
			case 3: //ACTIVATION
				break;
			case 0: //DEFAULT
			default:
				break;
		}
	}

	public final void addChild(Gui gui)
	{
		if (gui == null) return;
		if (gui == this) throw new IllegalArgumentException("Cannot make cyclical dependencies!");
		if (gui.parent != null) throw new IllegalArgumentException("Gui already has another parent!");

		gui.parent = this;
		this.childs.add(gui);
	}

	public final void removeChild(Gui gui)
	{
		if (gui.parent != this) throw new IllegalArgumentException("Gui does not belong to this parent!");

		gui.parent = null;
		this.childs.remove(gui);
	}

	public final int getDepth()
	{
		return this.parent.getDepth() + this.depth;
	}

	@Override
	public int compareTo(Gui o)
	{
		return this.getDepth() - o.getDepth();
	}

	public static boolean contains(Gui gui, float x, float y)
	{
		return x >= gui.x && y >= gui.y && x < gui.x + gui.width && y < gui.y + gui.height;
	}
}
