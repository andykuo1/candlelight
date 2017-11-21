package net.jimboi.test.suger.baron;

import net.jimboi.test.suger.canvas.LayeredCanvasPane;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Andy on 11/15/17.
 */
public class Baron
{
	public static void initialize(ViewPort view, InputHandler input, RenderHandler render, WorldHandler world, Stage primaryStage) throws Exception
	{
		final LayeredCanvasPane canvasPane = new LayeredCanvasPane(
				render.getNumOfLayers(), 640, 480);

		canvasPane.setFocusTraversable(true);
		canvasPane.addEventHandler(MouseEvent.MOUSE_MOVED, event ->
				view.setCursorPosition(event.getX(), event.getY()));
		canvasPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event ->
				view.setCursorPosition(event.getX(), event.getY()));
		canvasPane.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
				view.setCursorDown(true));
		canvasPane.addEventHandler(MouseEvent.MOUSE_RELEASED, event ->
				view.setCursorDown(false));
		canvasPane.addEventHandler(ScrollEvent.SCROLL, event ->
				view.move(event.getDeltaX(), event.getDeltaY()));
		canvasPane.addEventHandler(ZoomEvent.ZOOM, event ->
				view.zoom(event.getZoomFactor()));
		canvasPane.addEventFilter(KeyEvent.KEY_PRESSED, event ->
				input.onInput(event.getCode()));

		final BorderPane root = new BorderPane(canvasPane);

		final AnimationTimer timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				world.onUpdate();
				render.onDraw(canvasPane, view);
			}
		};

		Scene scene = new Scene(root);
		scene.setFill(Color.WHITE);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Application");
		primaryStage.show();

		world.onCreate();

		timer.start();
	}
}