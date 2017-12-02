package org.zilar.gui;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 11/28/17.
 */
public class GuiBase
{
	protected GuiBase parent;
	protected List<GuiBase> children = new LinkedList<>();
	protected int offsetDepth = 1;

	protected float screenX;
	protected float screenY;
	protected float screenW;
	protected float screenH;
	protected int screenDepth;

	protected GuiState prevState;
	protected GuiState nextState;

	protected boolean visible = true;
	protected boolean enabled = true;

	public GuiBase setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}

	public GuiBase setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}

	public GuiBase setDepth(int depth)
	{
		this.offsetDepth = depth;
		return this;
	}

	public void addGui(GuiBase gui)
	{
		if (gui.parent == this)
			throw new IllegalArgumentException("could not add child to gui - already a child of this gui");
		if (gui.parent != null)
			throw new IllegalArgumentException("could not add child to gui - must not be child of another gui");

		gui.parent = this;
		gui.screenDepth = this.screenDepth + gui.offsetDepth;
		this.children.add(gui);
	}

	public void removeGui(GuiBase gui)
	{
		if (gui.parent == null)
			throw new IllegalArgumentException("could not remove child from gui - already removed or is not a child");
		if (gui.parent != this)
			throw new IllegalArgumentException("could not remove child from gui - not a child of this gui");

		gui.parent = null;
		gui.screenDepth = gui.offsetDepth;
		this.children.remove(gui);
	}

	protected void updateState(GuiState state)
	{
		this.prevState = this.nextState;
		this.nextState = state;
	}

	protected void update()
	{
		//Update affected children
		for (GuiBase child : this.children)
		{
			child.update();
		}
	}

	public boolean contains(float x, float y)
	{
		return x >= this.screenX && x < this.screenX + this.screenW &&
				y >= this.screenY && y < this.screenY + this.screenH;
	}

	public Iterable<GuiBase> getChildren()
	{
		return this.children;
	}

	public GuiBase getParent()
	{
		return this.parent;
	}

	public boolean isVisible()
	{
		return this.visible;
	}

	public GuiState getState()
	{
		return this.nextState;
	}

	public float getScreenX()
	{
		return this.screenX;
	}

	public float getScreenY()
	{
		return this.screenY;
	}

	public float getScreenWidth()
	{
		return this.screenW;
	}

	public float getScreenHeight()
	{
		return this.screenH;
	}

	public int getScreenDepth()
	{
		return this.screenDepth;
	}
}
