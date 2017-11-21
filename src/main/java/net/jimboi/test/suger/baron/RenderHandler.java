package net.jimboi.test.suger.baron;

import net.jimboi.test.suger.canvas.LayeredCanvasPane;

/**
 * Created by Andy on 11/15/17.
 */
public interface RenderHandler
{
	void onDraw(LayeredCanvasPane canvas, ViewPort view);

	int getNumOfLayers();
}
