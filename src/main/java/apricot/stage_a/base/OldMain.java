package apricot.stage_a.base;

import apricot.base.OldGameEngine;
import apricot.base.scene.Scene;
import apricot.base.scene.SceneManager;
import apricot.stage_a.dood.SceneDood;
import apricot.base.render.OldRenderEngine;
import apricot.base.tick.OldTickEngine;
import apricot.base.window.OldWindow;
import apricot.base.window.input.InputEngine;

/**
 * Created by Andy on 4/27/17.
 */
public class OldMain
{
	public static Scene SCENE;

	public static OldTickEngine TICKENGINE;
	public static OldWindow WINDOW;
	public static InputEngine INPUTENGINE;
	public static OldRenderEngine RENDERENGINE;

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
