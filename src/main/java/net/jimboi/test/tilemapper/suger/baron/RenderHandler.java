package net.jimboi.test.tilemapper.suger.baron;

import net.jimboi.test.tilemapper.suger.canvas.LayeredCanvas;

import javafx.stage.Stage;

/**
 * Created by Andy on 11/15/17.
 */
public interface RenderHandler
{
	void onLoad(Stage stage, LayeredCanvas canvas, ViewPort view);

	void onDraw(Stage stage, LayeredCanvas canvas, ViewPort view);

	int getNumOfLayers();
}
