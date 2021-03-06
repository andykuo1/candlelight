package boron.stage_a.goblet.gui.base;

import boron.base.window.input.InputEngine;
import boron.base.window.input.InputLayer;
import boron.stage_a.goblet.Goblet;

import boron.bstone.camera.Camera;
import boron.bstone.camera.OrthographicCamera;
import boron.bstone.util.Direction;
import boron.bstone.window.view.ScreenSpace;
import boron.bstone.window.view.ViewPort;
import org.joml.Vector3f;
import boron.qsilver.poma.Poma;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiManager implements InputLayer
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

	private boolean inputSelect;

	public void update()
	{
		for(Gui gui : this.elements)
		{
			gui.onUpdate();
		}
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		this.inputSelect = Goblet.getGoblet().getInput().isInputDown("mouseleft");

		float mouseX = Goblet.getGoblet().getInput().getInputAmount("mousex");
		float mouseY = Goblet.getGoblet().getInput().getInputAmount("mousey");
		Vector3f mouse = this.screenSpace.getPoint2DFromScreen(mouseX, mouseY, new Vector3f());
		if (this.selector.update(this.elements.iterator(), mouse.x(), mouse.y(), this.inputSelect))
		{
			Goblet.getGoblet().getInput().consumeInput("mouseleft");
		}
	}

	@Override
	public int compareTo(InputLayer o)
	{
		return -1;
	}

	public void destroy()
	{
		this.elements.clear();
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
