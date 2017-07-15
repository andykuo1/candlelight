package net.jimboi.stage_c.gui;

import java.util.Iterator;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiSelector
{
	public float x;
	public float y;

	public int hoverTime;
	public boolean activeSelect;
	public Gui guiSelect;

	public void update(Iterator<Gui> guis, float selectX, float selectY, boolean select)
	{
		Gui next = null;
		while(guis.hasNext())
		{
			Gui gui = guis.next();
			if (gui.isSelectable(selectX, selectY))
			{
				if (next == null || next.compareTo(gui) < 0)
				{
					next = gui;
				}
			}
		}

		if (select)
		{
			//On press
			this.select(next, true);
		}
		else
		{
			if (this.guiSelect != null && this.activeSelect)
			{
				//On activation
				this.guiSelect.onGuiStateChanged(3);
				this.select(null, false);
			}
			else
			{
				//On hover
				this.select(next, false);
			}
		}
	}

	public void select(Gui gui, boolean active)
	{
		if (gui == null)
		{
			if (this.guiSelect != null)
			{
				//Return to normal state
				this.guiSelect.onGuiStateChanged(0);

				this.guiSelect = null;
				this.activeSelect = active;
				this.hoverTime = 0;
			}

			return;
		}

		if (this.guiSelect == gui)
		{
			this.hoverTime++;
		}
		else
		{
			if (this.guiSelect != null)
			{
				this.select(null, false);
			}

			this.guiSelect = gui;
			this.activeSelect = active;
			this.hoverTime = 0;

			if (this.activeSelect)
			{
				//Begin hover state
				this.guiSelect.onGuiStateChanged(1);
			}
			else
			{
				//Begin active state
				this.guiSelect.onGuiStateChanged(2);
			}
		}
	}
}
