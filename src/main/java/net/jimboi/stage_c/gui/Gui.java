package net.jimboi.stage_c.gui;

import org.zilar.sprite.NineSheet;

/**
 * Created by Andy on 7/12/17.
 */
public abstract class Gui
{
	protected GuiManager guiManager;

	private NineSheet borderTexture;

	private int x;
	private int y;
	private int width;
	private int height;
	private boolean visible;
	private int color;

	private Gui parent;
	private boolean dirty;

	public Gui(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.visible = true;
		this.dirty = true;
	}

	public void onGuiUpdate()
	{
		if (!this.visible) return;

		if (this.dirty)
		{
			this.updateGui();

			this.dirty = false;
		}
	}

	protected abstract void updateGui();

	protected abstract void onEnter();
	protected abstract void onLeave();

	protected abstract void onSelect();
	protected abstract void onAction();

	public final void markDirty()
	{
		this.dirty = true;
	}

	public final boolean isDirty()
	{
		return this.dirty;
	}

	public boolean contains(int x, int y)
	{
		return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
	}

	public void setBorderTexture(NineSheet ninesheet)
	{
		this.borderTexture = ninesheet;
	}

	public void setOffset(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.dirty = true;
	}

	public void setDimension(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.dirty = true;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	protected void setParent(Gui gui)
	{
		this.parent = gui;
	}

	public Gui getParent()
	{
		return this.parent;
	}

	public NineSheet getBorderTexture()
	{
		return this.borderTexture;
	}

	public int getColor()
	{
		return this.color;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getDepth()
	{
		return this.parent == null ? 0 : this.parent.getDepth() + 1;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public boolean isVisible()
	{
		return this.visible;
	}
}
