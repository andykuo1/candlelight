package net.jimboi.test.suger.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * Created by Andy on 11/9/17.
 */
public class ResizeableCanvasPane extends Pane
{
	private final Canvas canvas;

	public ResizeableCanvasPane(double width, double height)
	{
		this.canvas = new Canvas(width, height);
		this.getChildren().add(this.canvas);
	}

	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		final double x = this.snappedLeftInset();
		final double y = this.snappedTopInset();
		final double w = this.snapSize(this.getWidth()) - x - this.snappedRightInset();
		final double h = this.snapSize(this.getHeight()) - y - this.snappedBottomInset();

		this.canvas.setLayoutX(x);
		this.canvas.setLayoutY(y);
		this.canvas.setWidth(w);
		this.canvas.setHeight(h);
	}

	public final Canvas getCanvas()
	{
		return this.canvas;
	}
}
