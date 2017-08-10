package net.jimboi.boron.stage_a.goblet;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderHandler;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
import org.bstone.window.Window;
import org.bstone.window.input.InputManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.poma.Poma;

/**
 * Created by Andy on 8/9/17.
 */
public final class Goblet implements TickHandler, RenderHandler
{
	public static Goblet getGoblet()
	{
		return INSTANCE;
	}

	public static TickEngine getTickEngine()
	{
		return TICKENGINE;
	}

	public static RenderEngine getRenderEngine()
	{
		return RENDERENGINE;
	}

	public static boolean isDebugMode()
	{
		return DEBUG;
	}

	private static Goblet INSTANCE;
	private Goblet() {}

	private static TickEngine TICKENGINE;
	private static RenderEngine RENDERENGINE;

	private static boolean DEBUG;

	public static void main(String[] args)
	{
		Poma.makeSystemLogger();

		INSTANCE = new Goblet();

		TICKENGINE = new TickEngine(60, true, INSTANCE);
		RENDERENGINE = new RenderEngine(INSTANCE);

		TICKENGINE.run();
	}

	private Window window;
	private InputManager input;

	private GobletWorld world;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.window = new Window("Goblet", 640, 480);
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
	public void onLastUpdate(TickEngine tickEngine)
	{
		this.world.stop();

		RENDERENGINE.unload();

		this.input.clear();
		this.window.destroy();

		RENDERENGINE.destroy();
	}

	private GobletRenderer renderer;

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.renderer = new GobletRenderer();
		this.renderer.load(renderEngine);
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.renderer.unload(renderEngine);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine, double delta)
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

	public Window getWindow()
	{
		return this.window;
	}
}
