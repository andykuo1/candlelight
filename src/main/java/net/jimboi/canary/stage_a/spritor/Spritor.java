package net.jimboi.canary.stage_a.spritor;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Andy on 10/28/17.
 */
public class Spritor extends Pane
{
	public static void main(String[] args)
	{
		Application app = new Application() {
			@Override
			public void start(Stage primaryStage) throws Exception
			{
			}
		};
		Application.launch(args);
	}
}
