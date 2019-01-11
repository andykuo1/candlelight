package apricot.base;

import apricot.base.input.OldInputManager;
import apricot.base.scene.Scene;
import apricot.base.scene.SceneManager;
import apricot.base.asset.AssetManager;
import apricot.base.render.OldRenderEngine;
import apricot.base.render.OldRenderHandler;
import apricot.base.tick.OldTickEngine;
import apricot.base.tick.OldTickHandler;
import apricot.base.window.OldWindow;
import apricot.base.window.input.InputEngine;

import apricot.qsilver.poma.Poma;

import java.util.Arrays;

/**
 * Created by Andy on 7/5/17.
 */
@Deprecated
public final class OldGameEngine
{
	public static OldTickEngine TICKENGINE;
	public static OldWindow WINDOW;
	public static InputEngine INPUTENGINE;
	public static OldRenderEngine RENDERENGINE;

	public static AssetManager ASSETMANAGER;
	public static SceneManager SCENEMANAGER;

	private OldGameEngine() {}

	@SuppressWarnings("unchecked")
	public static Scene init(String classpath, String[] args)
	{
		Class<? extends Scene> sceneClass;
		try
		{
			Class<?> newclass = Class.forName(classpath);
			if (Scene.class.isAssignableFrom(newclass))
			{
				sceneClass = (Class<? extends Scene>) newclass;
			}
			else
			{
				throw new IllegalArgumentException("Not a scene! - Found invalid class with name: " + classpath);
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException("Unable to find class with name: " + classpath);
		}

		return init(sceneClass, args);
	}

	public static Scene init(Class<? extends Scene> sceneClass, String[] args)
	{
		Poma.makeSystemLogger();

		Poma.div();
		Poma.info("The Awesome Program: " + sceneClass.getSimpleName());
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		TICKENGINE = new OldTickEngine(60, true, new OldTickHandler()
		{
			@Override
			public void onFirstUpdate(OldTickEngine tickEngine)
			{
				System.out.println("BAMW!");
				RENDERENGINE.load();
			}

			@Override
			public void onPreUpdate()
			{
				if (!flag)
				{
					WINDOW.poll();

					if (WINDOW.shouldCloseWindow())
					{
						flag = true;
						SCENEMANAGER.stopScene();
					}
				}
			}

			@Override
			public void onFixedUpdate()
			{
				if (!SCENEMANAGER.isSetupMode())
				{
					WINDOW.updateInput();
				}
				SCENEMANAGER.update(0.02D);
			}

			private boolean flag = false;

			@Override
			public void onUpdate(double delta)
			{
				if (!flag)
				{
					WINDOW.clearScreenBuffer();
				}
				else if (!SCENEMANAGER.isActive())
				{
					TICKENGINE.stop();
				}

				RENDERENGINE.update(delta);

				if (!flag)
				{
					WINDOW.updateScreenBuffer();
				}
			}

			@Override
			public void onLastUpdate(OldTickEngine tickEngine)
			{
				ASSETMANAGER.destroy();
				RENDERENGINE.unload();
				WINDOW.destroy();
				RENDERENGINE.destroy();
			}
		});

		RENDERENGINE = new OldRenderEngine(new OldRenderHandler()
		{
			@Override
			public void onRenderLoad(OldRenderEngine renderEngine)
			{
			}

			@Override
			public void onRenderUnload(OldRenderEngine renderEngine)
			{
			}

			@Override
			public void onRenderUpdate(OldRenderEngine renderEngine, double delta)
			{
				SCENEMANAGER.renderUpdate(renderEngine);
				ASSETMANAGER.update(renderEngine);
			}
		});

		WINDOW = new OldWindow("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();
		OldInputManager.init(INPUTENGINE);

		SCENEMANAGER = new SceneManager();
		ASSETMANAGER = new AssetManager();

		Scene scene;
		try
		{
			scene = sceneClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid scene class! Must have defined default constructor!");
		}

		SCENEMANAGER.nextScene(scene);
		return scene;
	}

	public static void run()
	{
		if (TICKENGINE == null)
		{
			throw new IllegalStateException("Must call init first!");
		}

		TICKENGINE.run();
	}
}
