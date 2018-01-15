package net.jimboi.test.tilemapper.suger.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/22/17.
 */
public class LayeredCanvasPane extends Pane implements LayeredCanvas
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
	public void clear()
	{
		for(Canvas canvas : this.layers)
		{
			canvas.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	@Override
	public final Canvas getCanvas(int layer)
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
