package net.jimboi.stage_c.gui;

import org.bstone.input.InputManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiManager
{
	public List<Gui> guis = new ArrayList<>();
	public GuiSelector selector = new GuiSelector();

	public void addGui(Gui gui)
	{
		this.guis.add(gui);
	}

	public void removeGui(Gui gui)
	{
		this.guis.remove(gui);
	}

	public void update()
	{
		float mouseX = InputManager.getInputAmount("mousex");
		float mouseY = InputManager.getInputAmount("mousey");
		this.selector.update(this.guis.iterator(), mouseX, mouseY, InputManager.isInputDown("mouseleft"));
	}
}
