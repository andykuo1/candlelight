package net.jimboi.stage_c.gui.base;

import net.jimboi.stage_c.gui.GuiButton;
import net.jimboi.stage_c.gui.GuiFrame;
import net.jimboi.stage_c.gui.GuiPanel;

import org.bstone.input.InputManager;
import org.bstone.util.direction.Direction;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.view.ScreenSpace;
import org.bstone.window.view.ViewPort;
import org.joml.Vector3f;
import org.qsilver.poma.Poma;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiManager
{
	public final Set<Gui> elements = new TreeSet<>();
	public final Camera camera;
	public final ScreenSpace screenSpace;

	public GuiSelector selector = new GuiSelector();

	public GuiManager(OrthographicCamera camera, ViewPort viewport)
	{
		//TODO: Add support for a perspective camera?
		this.camera = camera;

		this.screenSpace = new ScreenSpace(viewport, this.camera, Direction.NORTHWEST, true, true);

		Gui frame = new GuiFrame();
		frame.setSize(9, 10);

		Gui panel = new GuiPanel();
		panel.setPosition(0.5F, 0.5F);
		panel.setSize(8, 9);
		frame.addChild(panel);

		Gui button = new GuiButton((gui) -> System.out.println("BOO!"));
		button.setPosition(1, 2);
		button.setSize(6, 1);
		panel.addChild(button);

		button = new GuiButton((gui) -> System.out.println("BOO 2!"));
		button.setPosition(1, 4);
		button.setSize(2, 1);
		panel.addChild(button);

		button = new GuiButton((gui) -> System.out.println("BOO 3!"));
		button.setPosition(5, 4);
		button.setSize(2, 1);
		button.setEnabled(false);
		panel.addChild(button);

		this.addGui(frame);
	}

	public Gui addGui(Gui gui)
	{
		return this.addGuiImpl(gui, 0);
	}

	private Gui addGuiImpl(Gui gui, int recur)
	{
		this.elements.add(gui);
		gui.guiManager = this;

		Iterator<Gui> iter = gui.getChilds();
		while(iter.hasNext())
		{
			Gui child = iter.next();
			if (child.guiManager == null)
			{
				if (recur > 100) throw new IllegalStateException("Gui hierarchy tree is too deep!");

				this.addGuiImpl(child, recur + 1);
			}
		}
		return gui;
	}

	public void removeGui(Gui gui)
	{
		this.removeGuiImpl(gui, 0);
	}

	private void removeGuiImpl(Gui gui, int recur)
	{
		this.elements.remove(gui);
		gui.guiManager = null;

		Iterator<Gui> iter = gui.getChilds();
		while(iter.hasNext())
		{
			Gui child = iter.next();
			if (child.guiManager == this)
			{
				if (recur > 100) throw new IllegalStateException("Gui hierarchy tree is too deep!");

				this.removeGuiImpl(child, recur + 1);
			}
			else if (child.guiManager != null)
			{
				Poma.warn("Found gui child managed by another manager - solved!");
				child.guiManager.removeGuiImpl(child, recur + 1);
			}
		}
	}

	public void update()
	{
		float mouseX = InputManager.getInputAmount("mousex");
		float mouseY = InputManager.getInputAmount("mousey");
		Vector3f mouse = this.screenSpace.getPoint2DFromScreen(mouseX, mouseY, new Vector3f());
		this.selector.update(this.elements.iterator(), mouse.x(), mouse.y(), InputManager.isInputDown("mouseleft"));

		for(Gui gui : this.elements)
		{
			gui.onUpdate();
		}
	}

	public GuiSelector getSelector()
	{
		return this.selector;
	}

	public Camera getCamera()
	{
		return this.camera;
	}
}
