package net.jimboi.stage_c.gui;

import org.bstone.input.InputManager;
import org.bstone.window.Window;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/12/17.
 */
public class GuiManager
{
	private Window window;
	private List<Gui> guis = new ArrayList<>();
	private Gui focus;
	private boolean active;

	public GuiManager(Window window)
	{
		this.window = window;
	}

	public void update()
	{
		float x = InputManager.getInputAmount("mousex");
		float y = InputManager.getInputAmount("mousey");

		Vector2f mouse = new Vector2f(x, y);
		this.window.toNormalizedDeviceCoords(mouse, mouse);
		mouse.mul(this.window.getWidth(), this.window.getHeight());

		Gui gui = this.getGuiAt((int) mouse.x(), (int) mouse.y());
		this.setFocus(gui);

		if (this.focus != null)
		{
			if (InputManager.isInputDown("mouseleft"))
			{
				if (!this.active)
				{
					this.active = true;
					this.focus.onSelect();
				}
			}
			else if (this.active)
			{
				this.active = false;
				this.focus.onAction();
			}
		}
	}

	public void setFocus(Gui gui)
	{
		if (this.focus != gui)
		{
			this.focus.onLeave();
			this.focus = gui;
			this.active = false;
			if (this.focus != null)
			{
				this.focus.onEnter();
			}
		}
	}

	public Gui getFocus()
	{
		return this.focus;
	}

	public boolean hasFocus()
	{
		return this.focus != null;
	}

	public <T extends Gui> T addGui(T gui)
	{
		this.guis.add(gui);
		gui.guiManager = this;
		return gui;
	}

	public void removeGui(Gui gui)
	{
		this.guis.remove(gui);
		gui.guiManager = null;
	}

	public Gui getGuiAt(int x, int y)
	{
		Gui selected = null;
		int selectedDepth = 0;
		for(Gui gui : this.guis)
		{
			if (gui.contains(x, y))
			{
				if (selected == null || selectedDepth < gui.getDepth())
				{
					selected = gui;
					selectedDepth = selected.getDepth();
				}
			}
		}
		return selected;
	}

	public List<Gui> getGuis()
	{
		return this.guis;
	}
}
