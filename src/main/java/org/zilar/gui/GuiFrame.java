package org.zilar.gui;

import org.bstone.window.view.ViewPort;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andy on 11/28/17.
 */
public final class GuiFrame extends GuiBase
{
	private ViewPort viewport;
	private GuiBase focusElement;

	private float cursorX;
	private float cursorY;
	private boolean cursorAction;

	public GuiFrame(ViewPort viewport)
	{
		this.viewport = viewport;
	}

	@Override
	public void update()
	{
		if (this.screenW != this.viewport.getWidth() || this.screenH != this.viewport.getHeight())
		{
			this.screenW = this.viewport.getWidth();
			this.screenH = this.viewport.getHeight();

			super.update();
		}

		GuiBase focus = this.getElementAt(this.cursorX, this.cursorY);

		if (this.focusElement != focus)
		{
			if (this.focusElement != null) this.focusElement.updateState(null);
			this.focusElement = focus;
		}

		if (this.focusElement != null)
		{
			if (this.cursorAction)
			{
				this.focusElement.updateState(GuiState.CLICK);
				this.cursorAction = false;
			}
			else
			{
				this.focusElement.updateState(GuiState.HOVER);
			}
		}
	}

	public GuiBase getElementAt(float x, float y)
	{
		GuiBase result = null;

		Queue<GuiBase> guis = new LinkedList<>();
		for(GuiBase guiBase : this.getChildren())
		{
			guis.add(guiBase);
			while(!guis.isEmpty())
			{
				GuiBase gui = guis.poll();
				if (gui.contains(x, y))
				{
					if (result == null || result.getScreenDepth() < gui.getScreenDepth())
					{
						result = gui;

						for (GuiBase child : gui.getChildren())
						{
							guis.add(child);
						}
					}
				}
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends GuiBase> T getElementAt(Class<T> type, float x, float y)
	{
		T result = null;

		Queue<GuiBase> guis = new LinkedList<>();
		for(GuiBase guiBase : this.getChildren())
		{
			guis.add(guiBase);
			while(!guis.isEmpty())
			{
				GuiBase gui = guis.poll();
				if (type.isInstance(gui) && gui.contains(x, y))
				{
					if (result == null || result.getScreenDepth() < gui.getScreenDepth())
					{
						result = (T) gui;

						for (GuiBase child : gui.getChildren())
						{
							guis.add(child);
						}
					}
				}
			}
		}

		return result;
	}

	public void setCursorPosition(double x, double y)
	{
		this.cursorX = (float) x;
		this.cursorY = (float) y;
	}

	public void setCursorAction(boolean action)
	{
		this.cursorAction = action;
	}

	public ViewPort getViewport()
	{
		return this.viewport;
	}

	public Iterator<GuiBase> getElements()
	{
		return new GuiIterator(this);
	}
}
