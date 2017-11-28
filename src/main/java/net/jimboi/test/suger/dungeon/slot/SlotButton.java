package net.jimboi.test.suger.dungeon.slot;

import org.bstone.util.function.Procedure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/22/17.
 */
public class SlotButton extends DungeonSlot
{
	public static final int WIDTH = 80;
	public static final int HEIGHT = 24;

	private static final double TEXT_WIDTH = 10;
	private static final double TEXT_HEIGHT = 10;

	private final String text;
	private double textX;
	private double textY;
	private double textWidth;

	private final Procedure action;

	public SlotButton(String text, Procedure action)
	{
		this.text = text;
		this.textWidth = this.text.length() * TEXT_WIDTH;
		this.action = action;

		this.setSize(WIDTH, HEIGHT);
	}

	@Override
	public DungeonSlot setPosition(double x, double y)
	{
		super.setPosition(x, y);

		this.textX = this.x + this.width / 2 - (this.textWidth / 2);
		this.textY = this.y + this.height / 2 + TEXT_HEIGHT / 2;

		return this;
	}

	@Override
	protected void onActivate()
	{
		this.action.apply();
	}

	@Override
	protected void onRender(GraphicsContext g)
	{
		g.setFill(Color.BLACK);
		g.fillText(this.text, this.textX, this.textY);
	}
}
