package net.jimboi.apricot.stage_a.base;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.scene.Scene;
import net.jimboi.apricot.base.scene.SceneManager;
import net.jimboi.apricot.stage_a.dood.SceneDood;
import net.jimboi.boron.base_ab.render.OldRenderEngine;
import net.jimboi.boron.base_ab.tick.OldTickEngine;
import net.jimboi.boron.base_ab.window.OldWindow;
import net.jimboi.boron.base_ab.window.input.InputEngine;

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
