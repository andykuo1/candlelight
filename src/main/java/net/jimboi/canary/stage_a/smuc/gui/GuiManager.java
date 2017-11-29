package net.jimboi.canary.stage_a.smuc.gui;

import org.bstone.window.view.ViewPort;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Andy on 11/28/17.
 */
public class GuiManager
{
	private final ViewPort viewport;
	private final GuiFrame frame;

	private List<GuiBase> elements = new LinkedList<>();

	private float cursorX;
	private float cursorY;
	private boolean cursorAction;

	private GuiBase focusElement;

	public GuiManager(ViewPort viewport)
	{
		this.viewport = viewport;
		this.frame = new GuiFrame();
		this.frame.manager = this;
	}

	public void addElement(GuiBase element)
	{
		if (this.elements.contains(element)) throw new IllegalArgumentException("element already belongs to manager");

		this.elements.add(element);
	}

	public void removeElement(GuiBase element)
	{
		if (!this.elements.contains(element)) throw new IllegalArgumentException("element is does not belong to manager");

		this.elements.remove(element);

		Queue<GuiBase> guis = new LinkedList<>();
		for(GuiBase guiBase : element.getChildren())
		{
			guis.add(guiBase);
			while(!guis.isEmpty())
			{
				GuiBase gui = guis.poll();
				this.elements.remove(gui);

				for (GuiBase child : gui.getChildren())
				{
					guis.add(child);
				}
			}
		}
	}

	public void update()
	{
		//Should update on resize also
		this.frame.updateScreenSize(this.viewport.getWidth(), this.viewport.getHeight());
		this.frame.update();

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
		for(GuiBase guiBase : this.frame.getChildren())
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
		for(GuiBase guiBase : this.frame.getChildren())
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

	public GuiFrame getFrame()
	{
		return this.frame;
	}

	public ViewPort getViewport()
	{
		return this.viewport;
	}

	public Iterable<GuiBase> getElements()
	{
		return this.elements;
	}
}
