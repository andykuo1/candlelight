package boron.stage_a.goblet;

import boron.base.render.OldRenderEngine;
import boron.base.render.OldRenderHandler;
import boron.base.tick.OldTickEngine;
import boron.base.tick.OldTickHandler;
import boron.base.window.OldWindow;
import boron.base.window.input.InputManager;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import boron.qsilver.poma.Poma;

/**
 * Created by Andy on 8/9/17.
 */
public final class Goblet implements OldTickHandler, OldRenderHandler
{
	public static Goblet getGoblet()
	{
		return INSTANCE;
	}

	public static OldTickEngine getTickEngine()
	{
		return TICKENGINE;
	}

	public static OldRenderEngine getRenderEngine()
	{
		return RENDERENGINE;
	}

	public static boolean isDebugMode()
	{
		return DEBUG;
	}

	private static Goblet INSTANCE;
	private Goblet() {}

	private static OldTickEngine TICKENGINE;
	private static OldRenderEngine RENDERENGINE;

	private static boolean DEBUG;

	public static void main(String[] args)
	{
		Poma.makeSystemLogger();

		INSTANCE = new Goblet();

		TICKENGINE = new OldTickEngine(60, true, INSTANCE);
		RENDERENGINE = new OldRenderEngine(INSTANCE);

		TICKENGINE.run();
	}

	private OldWindow window;
	private InputManager input;

	private GobletWorld world;

	@Override
	public void onFirstUpdate(OldTickEngine tickEngine)
	{
		this.window = new OldWindow("Goblet", 640, 480);
		this.input = new InputManager(this.window.getInputEngine());

		this.input.registerKey("_debug", GLFW.GLFW_KEY_P);

		RENDERENGINE.load();

		this.world = new GobletWorld();
		this.world.start();
	}

	@Override
	public void onPreUpdate()
	{
		this.window.poll();

		if (this.window.shouldCloseWindow())
		{
			TICKENGINE.stop();
		}
	}

	@Override
	public void onFixedUpdate()
	{
		this.window.updateInput();

		if (this.input.isInputReleased("_debug"))
		{
			DEBUG = !DEBUG;
		}

		this.world.update();
	}

	@Override
	public void onUpdate(double delta)
	{
		this.window.clearScreenBuffer();
		{
			RENDERENGINE.update(delta);
		}
		this.window.updateScreenBuffer();
	}

	@Override
	public void onLastUpdate(OldTickEngine tickEngine)
	{
		this.world.stop();

		RENDERENGINE.unload();

		this.input.clear();
		this.window.destroy();

		RENDERENGINE.destroy();
	}

	private GobletRenderer renderer;

	@Override
	public void onRenderLoad(OldRenderEngine renderEngine)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.renderer = new GobletRenderer();
		this.renderer.load(renderEngine);
	}

	@Override
	public void onRenderUnload(OldRenderEngine renderEngine)
	{
		this.renderer.unload(renderEngine);
	}

	@Override
	public void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{
		this.renderer.update(renderEngine, delta);
	}

	public GobletWorld getWorld()
	{
		return this.world;
	}

	public GobletRenderer getRender()
	{
		return this.renderer;
	}

	public InputManager getInput()
	{
		return this.input;
	}

	public OldWindow getWindow()
	{
		return this.window;
	}
}
