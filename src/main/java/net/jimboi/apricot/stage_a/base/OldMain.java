package net.jimboi.apricot.stage_a.base;

import net.jimboi.apricot.base.GameEngine;
import net.jimboi.apricot.stage_a.dood.SceneDood;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.qsilver.render.RenderEngine;
import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneManager;

/**
 * Created by Andy on 4/27/17.
 */
public class OldMain
{
	public static Scene SCENE;

	public static TickEngine TICKENGINE;
	public static Window WINDOW;
	public static InputEngine INPUTENGINE;
	public static RenderEngine RENDERENGINE;

	public static SceneManager SCENEMANAGER;

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			String classpath = args[0];
			SCENE = GameEngine.init(classpath, new String[]{});
		}
		else
		{
			SCENE = GameEngine.init(SceneDood.class, new String[]{});
		}

		WINDOW = GameEngine.WINDOW;
		INPUTENGINE = GameEngine.INPUTENGINE;
		TICKENGINE = GameEngine.TICKENGINE;
		RENDERENGINE = GameEngine.RENDERENGINE;
		SCENEMANAGER = GameEngine.SCENEMANAGER;
		GameEngine.run();
	}
}
