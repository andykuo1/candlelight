package net.jimboi.apricot.stage_a.base;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.stage_a.dood.SceneDood;

import org.bstone.render.RenderEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.bstone.window.input.InputEngine;
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
			SCENE = OldGameEngine.init(classpath, new String[]{});
		}
		else
		{
			SCENE = OldGameEngine.init(SceneDood.class, new String[]{});
		}

		WINDOW = OldGameEngine.WINDOW;
		INPUTENGINE = OldGameEngine.INPUTENGINE;
		TICKENGINE = OldGameEngine.TICKENGINE;
		RENDERENGINE = OldGameEngine.RENDERENGINE;
		SCENEMANAGER = OldGameEngine.SCENEMANAGER;
		OldGameEngine.run();
	}
}
