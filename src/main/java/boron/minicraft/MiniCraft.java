package boron.minicraft;

import boron.minicraft.ld22.Game;

import boron.base.tick.OldTickEngine;
import boron.base.tick.OldTickHandler;
import boron.base.window.OldWindow;
import boron.base.window.input.InputManager;

import boron.bstone.camera.Camera;
import boron.bstone.camera.OrthographicCamera;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * Created by Andy on 8/3/17.
 */
public class MiniCraft implements OldTickHandler
{
	private static final MiniCraft INSTANCE = new MiniCraft();
	private MiniCraft(){}

	public static MiniCraft getMiniCraft()
	{
		return INSTANCE;
	}

	public static void main(String[] args)
	{
		INSTANCE.tickEngine = new OldTickEngine(60, true, INSTANCE);
		INSTANCE.tickEngine.run();
	}

	public OldTickEngine tickEngine;

	public OldWindow window;
	public InputManager inputManager;

	public Camera camera;
	public MiniRenderer renderer;

	public Game world;

	@Override
	public void onFirstUpdate(OldTickEngine tickEngine)
	{
		System.out.println("LWJGL " + Version.getVersion());
		System.out.println("JOML 1.9.2");

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!GLFW.glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		this.window = new OldWindow("MiniCraft", 640, 480);
		this.inputManager = new InputManager(this.window.getInputEngine());

		this.inputManager.registerKey("up", GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_W);
		this.inputManager.registerKey("down", GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_S);
		this.inputManager.registerKey("left", GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_A);
		this.inputManager.registerKey("right", GLFW.GLFW_KEY_RIGHT, GLFW.GLFW_KEY_D);
		this.inputManager.registerKey("attack", GLFW.GLFW_KEY_X);
		this.inputManager.registerKey("menu", GLFW.GLFW_KEY_C);

		this.camera = new OrthographicCamera(640, 480);

		this.renderer = new MiniRenderer(this.camera);
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
	public void onLastUpdate(OldTickEngine tickEngine)
	{
		this.renderer.unload();

		this.inputManager.clear();
		this.window.destroy();

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
}
