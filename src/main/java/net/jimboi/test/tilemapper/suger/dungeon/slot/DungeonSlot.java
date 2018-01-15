package net.jimboi.test.tilemapper.suger.dungeon.slot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/22/17.
 */
public abstract class DungeonSlot
{
	public static final int OUTLINE_ROUND_RADIUS = 5;
	public static final int OUTLINE_LINE_WIDTH = 4;
	public static final int INACTIVE_LINE_WIDTH = 1;

	private static final Color COLOR_BACKGROUND = Color.WHITE.deriveColor(0, 1, 1, 0.3);
	public static final Color COLOR_DISABLED_OVERLAY = Color.DARKGRAY.deriveColor(0, 1, 1, 0.5);

	public double x;
	public double y;
	public double width = 32;
	public double height = 32;
	public boolean enabled = true;

	public DungeonSlotGroup group;

	public Color outline = Color.DARKGRAY;

	public KeyCode hotkey;

	public DungeonSlot setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	public DungeonSlot setSize(double width, double height)
	{
		this.width = width;
		this.height = height;
		return this;
	}

	public DungeonSlot setColor(Color color)
	{
		this.outline = color;
		return this;
	}

	public DungeonSlot setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}

	public DungeonSlot setHotkey(KeyCode key)
	{
		this.hotkey = key;
		return this;
	}

	public void activate()
	{
		this.onActivate();

		if (this.group != null)
		{
			this.group.onSlotActivate(this);
		}
	}

	protected abstract void onActivate();

	public void render(GraphicsContext g)
	{
		g.setFill(COLOR_BACKGROUND);
		g.fillRect(this.x, this.y, this.width, this.height);

		g.setLineWidth(this.enabled ? OUTLINE_LINE_WIDTH : INACTIVE_LINE_WIDTH);
		g.setStroke(this.outline);
		g.strokeRoundRect(this.x, this.y, this.width, this.height,
				OUTLINE_ROUND_RADIUS, OUTLINE_ROUND_RADIUS);

		this.onRender(g);

		if (!this.enabled)
		{
			g.setFill(COLOR_DISABLED_OVERLAY);
			g.fillRoundRect(this.x, this.y, this.width, this.height,
					OUTLINE_ROUND_RADIUS, OUTLINE_ROUND_RADIUS);
		}
	}

	public void renderHotkey(GraphicsContext g)
	{
		g.setFill(Color.BLACK);
		g.fillText(this.hotkey.getName(), this.x + this.width - 5, this.y + this.height + 5);
	}

	protected abstract void onRender(GraphicsContext g);

	public boolean contains(double x, double y)
	{
		return x >= this.x && x < this.x + this.width &&
				y >= this.y && y < this.y + this.height;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public KeyCode getHotkey()
	{
		return this.hotkey;
	}

	public Color getColor()
	{
		return this.outline;
	}
}
