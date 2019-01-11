package canary.test.tilemapper.suger.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

/**
 * Created by Andy on 11/22/17.
 */
public interface LayeredCanvas
{
	void clear();

	Canvas getCanvas(int layer);

	WritableImage toWritableImage(Rectangle2D viewport);

	double getWidth();
	double getHeight();
}
