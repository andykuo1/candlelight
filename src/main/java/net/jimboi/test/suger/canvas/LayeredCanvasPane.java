package net.jimboi.test.suger.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * Created by Andy on 11/9/17.
 */
public class LayeredCanvasPane extends Pane
{
	private final Canvas[] layers;

	public LayeredCanvasPane(int layers, double width, double height)
	{
		this.layers = new Canvas[layers];
		for(int i = 0; i < this.layers.length; ++i)
		{
			this.layers[i] = new Canvas(width, height);
			this.getChildren().add(this.layers[i]);
			this.layers[i].toFront();
		}
	}

	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		final double x = this.snappedLeftInset();
		final double y = this.snappedTopInset();
		final double w = this.snapSize(this.getWidth()) - x - this.snappedRightInset();
		final double h = this.snapSize(this.getHeight()) - y - this.snappedBottomInset();

		for(Canvas canvas : this.layers)
		{
			canvas.setLayoutX(x);
			canvas.setLayoutY(y);
			canvas.setWidth(w);
			canvas.setHeight(h);
		}
	}

	public void clear()
	{
		for(Canvas canvas : this.layers)
		{
			canvas.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	public final Canvas getCanvas(int layer)
	{
		return this.layers[layer];
	}
}
