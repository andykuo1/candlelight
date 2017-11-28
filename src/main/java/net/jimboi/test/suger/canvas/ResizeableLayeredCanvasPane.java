package net.jimboi.test.suger.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/9/17.
 */
public class ResizeableLayeredCanvasPane extends Pane implements LayeredCanvas
{
	private final Canvas[] layers;

	public ResizeableLayeredCanvasPane(int layers, double width, double height)
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

	@Override
	public void clear()
	{
		for(Canvas canvas : this.layers)
		{
			canvas.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	@Override
	public Canvas getCanvas(int layer)
	{
		return this.layers[layer];
	}

	@Override
	public WritableImage toWritableImage(Rectangle2D viewport)
	{
		SnapshotParameters params = new SnapshotParameters();
		params.setViewport(viewport);
		params.setFill(Color.TRANSPARENT);
		return this.snapshot(params, null);
	}
}
