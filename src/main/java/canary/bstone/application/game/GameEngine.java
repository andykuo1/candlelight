package canary.bstone.application.game;

import canary.bstone.application.Application;
import canary.bstone.application.FrameCounter;
import canary.bstone.application.Framework;
import canary.bstone.application.service.RenderService;
import canary.bstone.application.service.RenderServiceManager;
import canary.bstone.application.service.TickService;
import canary.bstone.application.service.TickServiceManager;
import canary.bstone.asset.AssetManager;
import canary.bstone.input.InputEngine;
import canary.bstone.input.device.Keyboard;
import canary.bstone.input.device.Mouse;
import canary.bstone.json.JSONObject;
import canary.bstone.json.JSONString;
import canary.bstone.render.RenderEngine;
import canary.bstone.resource.BitmapLoader;
import canary.bstone.resource.MeshLoader;
import canary.bstone.resource.ProgramLoader;
import canary.bstone.resource.ShaderLoader;
import canary.bstone.resource.TextureAtlasLoader;
import canary.bstone.resource.TextureLoader;
import canary.bstone.scene.SceneManager;
import canary.bstone.tick.TickEngine;
import canary.bstone.util.parser.json.JSONFormatParser;
import canary.bstone.window.Window;
import org.lwjgl.opengl.GL20;
import canary.qsilver.ResourceLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Andy on 10/17/17.
 */
public class GameEngine implements Framework, Game
{
	protected final Window window;
	protected final InputEngine inputEngine;

	protected Mouse mouse;
	protected Keyboard keyboard;

	protected final RenderEngine renderEngine;
	protected final TickEngine tickEngine;

	protected final AssetManager assetManager;
	protected final SceneManager sceneManager;

	private FrameCounter frameCounter;

	public GameEngine()
	{
		this.window = new Window();
		this.inputEngine = new InputEngine();

		this.tickEngine = new TickEngine(60, new TickServiceManager());
		this.getTickServices().setHandler(this.tickEngine);

		this.renderEngine = new RenderEngine(this.window, new RenderServiceManager());
		this.getRenderServices().setHandler(this.renderEngine);

		this.assetManager = new AssetManager();
		this.sceneManager = new SceneManager(this.getRenderServices());

		this.frameCounter = new FrameCounter();
	}

	@Override
	public void onApplicationCreate(Application app) throws Exception
	{
		Window.initializeGLFW();
	}

	@Override
	public void onApplicationStart(Application app)
	{
		this.window.setSize(640, 480).setTitle("Application").show();

		this.mouse = new Mouse(this.window, this.inputEngine);
		this.keyboard = new Keyboard(this.window, this.inputEngine);

		this.inputEngine.addContext("main");

		this.assetManager.registerLoader("bitmap", new BitmapLoader());
		this.assetManager.registerLoader("texture", new TextureLoader(this.assetManager));
		this.assetManager.registerLoader("texture_atlas", new TextureAtlasLoader(this.assetManager));
		this.assetManager.registerLoader("program", new ProgramLoader(this.assetManager));
		this.assetManager.registerLoader("vertex_shader", new ShaderLoader(GL20.GL_VERTEX_SHADER));
		this.assetManager.registerLoader("fragment_shader", new ShaderLoader(GL20.GL_FRAGMENT_SHADER));
		this.assetManager.registerLoader("mesh", new MeshLoader());

		this.getRenderServices().startService("framework", new Game.Render(this));
		this.getRenderServices().startService("frame", new RenderService()
		{
			@Override
			protected void onRenderLoad(RenderEngine renderEngine)
			{

			}

			@Override
			protected void onRenderUnload(RenderEngine renderEngine)
			{

			}

			@Override
			protected void onRenderUpdate(RenderEngine renderEngine)
			{
				GameEngine.this.frameCounter.frame();
			}
		});
		this.getTickServices().startService("framework", new Game.Tick(this));
		this.getTickServices().startService("tick", new TickService()
		{
			@Override
			protected void onFirstUpdate(TickEngine tickEngine)
			{

			}

			@Override
			protected void onLastUpdate(TickEngine tickEngine)
			{

			}

			@Override
			protected void onFixedUpdate()
			{
				GameEngine.this.frameCounter.tick();
				GameEngine.this.inputEngine.poll();
			}
		});

		app.startEngine(this.inputEngine);
		app.startEngine(this.renderEngine);
		app.startEngine(this.tickEngine);
	}

	@Override
	public void onApplicationStop(Application app)
	{
		this.getTickServices().stopService("framework");
		this.getTickServices().stopService("tick");
		this.getRenderServices().stopService("framework");
		this.getRenderServices().stopService("frame");

		this.sceneManager.destroy();
		this.assetManager.destroy();
	}

	@Override
	public void onApplicationDestroy(Application app)
	{
		Window.terminateGLFW();
	}

	@Override
	public void onApplicationUpdate(Application app)
	{
		if (this.window.shouldWindowClose())
		{
			app.stop();
		}

		this.assetManager.update();
		this.frameCounter.poll();
	}

	@Override
	public void onFixedUpdate()
	{
		this.sceneManager.update();
	}

	public Window getWindow()
	{
		return window;
	}

	public TickEngine getTickEngine()
	{
		return tickEngine;
	}

	public TickServiceManager getTickServices()
	{
		return (TickServiceManager) this.tickEngine.getTickable();
	}

	public RenderEngine getRenderEngine()
	{
		return renderEngine;
	}

	public RenderServiceManager getRenderServices()
	{
		return (RenderServiceManager) this.renderEngine.getRenderable();
	}

	public InputEngine getInputEngine()
	{
		return inputEngine;
	}

	public Mouse getMouse()
	{
		return this.mouse;
	}

	public Keyboard getKeyboard()
	{
		return this.keyboard;
	}

	public AssetManager getAssetManager()
	{
		return this.assetManager;
	}

	public SceneManager getSceneManager()
	{
		return this.sceneManager;
	}

	protected static void loadAssetsFromLocation(AssetManager assets, ResourceLocation location)
	{
		try
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(location.getFilePath())))
			{
				JSONFormatParser parser = new JSONFormatParser(256);
				JSONObject obj = (JSONObject) parser.parse(reader);

				for(String name : obj.names())
				{
					assets.registerResourceLocation(name,
							new ResourceLocation(((JSONString) obj.get(name)).get()));
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
