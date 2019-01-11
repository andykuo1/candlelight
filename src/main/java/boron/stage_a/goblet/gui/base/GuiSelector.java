package boron.stage_a.goblet.gui.base;

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

	public Gui guiFocus;

	public boolean update(Iterator<Gui> guis, float selectX, float selectY, boolean select)
	{
		this.x = selectX;
		this.y = selectY;

		Gui next = null;

		if (this.guiFocus == null)
		{
			while (guis.hasNext())
			{
				Gui gui = guis.next();
				if (gui.isSolid(this.x, this.y))
				{
					if (next == null || next.compareTo(gui) < 0)
					{
						next = gui;
					}
				}
			}
		}
		else
		{
			next = this.guiFocus;
		}

		if (next == null || !next.isEnabled())
		{
			//Early out of selection
			this.select(null, false);
		}
		else if (select)
		{
			//On press
			this.select(next, true);
		}
		else
		{
			if (this.guiSelect != null && this.activeSelect)
			{
				//On activation
				this.guiSelect.setGuiState(3);
				this.select(null, false);
				return true;
			}
			else
			{
				//On hover
				this.select(next, false);
			}
		}

		return next != null;
	}

	public void select(Gui gui, boolean active)
	{
		if (gui == null || !gui.isSelectable(active))
		{
			if (this.guiSelect != null)
			{
				//Return to normal state
				this.guiSelect.setGuiState(0);

				this.guiSelect = null;
				this.activeSelect = active;
				this.hoverTime = 0;
			}
		}
		else if (this.activeSelect == active && this.guiSelect == gui)
		{
			this.hoverTime++;
		}
		else
		{
			if (this.guiSelect != null && this.guiSelect != gui)
			{
				this.select(null, false);
			}

			this.guiSelect = gui;
			this.activeSelect = active;
			this.hoverTime = 0;

			if (!this.activeSelect)
			{
				//Begin hover state
				this.guiSelect.setGuiState(1);
			}
			else
			{
				//Begin activeActors state
				this.guiSelect.setGuiState(2);
			}
		}
	}

	public boolean hasFocus(Gui gui)
	{
		return gui == this.guiFocus;
	}

	public void grabFocus(Gui gui)
	{
		this.guiFocus = gui;
	}

	public float getX()
	{
		return this.x;
	}

	public float getY()
	{
		return this.y;
	}
}
