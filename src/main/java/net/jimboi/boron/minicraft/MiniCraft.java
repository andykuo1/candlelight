package net.jimboi.boron.minicraft;

import com.mojang.ld22.Game;

import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
import org.bstone.window.Window;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.input.InputManager;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 8/3/17.
 */
public class MiniCraft implements TickHandler
{
	private static final MiniCraft INSTANCE = new MiniCraft();
	private MiniCraft(){}

	public static MiniCraft getMiniCraft()
	{
		return INSTANCE;
	}

	public static void main(String[] args)
	{
		INSTANCE.tickEngine = new TickEngine(60, true, INSTANCE);
		INSTANCE.tickEngine.run();
	}

	public TickEngine tickEngine;

	public Window window;
	public InputManager inputManager;

	public Camera camera;
	public Renderer renderer;

	public Game world;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.window = new Window("MiniCraft", 640, 480);
		this.inputManager = new InputManager(this.window.getInputEngine());

		this.inputManager.registerKey("up", GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_W);
		this.inputManager.registerKey("down", GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_S);
		this.inputManager.registerKey("left", GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_A);
		this.inputManager.registerKey("right", GLFW.GLFW_KEY_RIGHT, GLFW.GLFW_KEY_D);
		this.inputManager.registerKey("attack", GLFW.GLFW_KEY_X);
		this.inputManager.registerKey("menu", GLFW.GLFW_KEY_C);

		this.camera = new OrthographicCamera(640, 480);

		this.renderer = new Renderer(this.camera);
		this.renderer.load();

		this.world = new Game(this.renderer.bmpScreen);
		this.world.init();
	}

	@Override
	public void onPreUpdate()
	{
		this.window.poll();

		if (this.window.shouldCloseWindow())
		{
			this.tickEngine.stop();
		}
	}

	@Override
	public void onFixedUpdate()
	{
		this.window.updateInput();

		//UPDATE
		this.world.tick();
	}

	@Override
	public void onUpdate(double delta)
	{
		this.window.clearScreenBuffer();
		{
			this.renderer.clearRenders();
			this.world.render(this.renderer);
			this.renderer.render();
		}
		this.window.updateScreenBuffer();
	}

	@Override
	public void onLastUpdate(TickEngine tickEngine)
	{
		this.renderer.unload();

		this.inputManager.clear();
		this.window.destroy();
	}
}
