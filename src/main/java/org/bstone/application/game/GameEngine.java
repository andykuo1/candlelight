package org.bstone.application.game;

import org.bstone.application.Application;
import org.bstone.application.Framework;
import org.bstone.asset.AssetManager;
import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.render.RenderEngine;
import org.bstone.resource.BitmapLoader;
import org.bstone.resource.MeshLoader;
import org.bstone.resource.ProgramLoader;
import org.bstone.resource.ShaderLoader;
import org.bstone.resource.TextureAtlasLoader;
import org.bstone.resource.TextureLoader;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.lwjgl.opengl.GL20;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 10/17/17.
 */
public class GameEngine implements Framework
{
	protected final Game handler;

	protected final Window window;
	protected final InputEngine inputEngine;
	protected InputContext input;

	protected final RenderEngine renderEngine;
	protected final TickEngine tickEngine;

	protected final AssetManager assetManager;

	private double timeCounter;

	public GameEngine(Game handler)
	{
		this.handler = handler;

		this.window = new Window();
		this.inputEngine = new InputEngine(this.window);
		this.tickEngine = new TickEngine(60, true, this.handler);
		this.renderEngine = new RenderEngine(this.window, this.tickEngine, this.handler);

		ResourceLocation assets = this.handler.getAssetLocation();
		if (assets != null)
		{
			this.assetManager = new AssetManager(assets);
		}
		else
		{
			this.assetManager = new AssetManager();
		}
	}

	@Override
	public void onApplicationCreate(Application app) throws Exception
	{
		Window.initializeGLFW();
	}

	@Override
	public void onApplicationStart(Application app)
	{
		this.window.create("Application", 640, 480);

		this.assetManager.registerLoader("bitmap", new BitmapLoader());
		this.assetManager.registerLoader("texture", new TextureLoader(this.assetManager));
		this.assetManager.registerLoader("texture_atlas", new TextureAtlasLoader(this.assetManager));
		this.assetManager.registerLoader("program", new ProgramLoader(this.assetManager));
		this.assetManager.registerLoader("vertex_shader", new ShaderLoader(GL20.GL_VERTEX_SHADER));
		this.assetManager.registerLoader("fragment_shader", new ShaderLoader(GL20.GL_FRAGMENT_SHADER));
		this.assetManager.registerLoader("mesh", new MeshLoader());

		app.startEngine(this.inputEngine);
		app.startEngine(this.renderEngine);
		app.startEngine(this.tickEngine);

		this.timeCounter = System.currentTimeMillis();
	}

	@Override
	public void onApplicationStop(Application app)
	{
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
		this.assetManager.update();

		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;

			System.out.print("[");
			{
				System.out.print("UPS: " + this.tickEngine.getUpdateCounter().get());
				System.out.print(" || ");
				System.out.print("FPS: " + this.renderEngine.getFrameCounter().get());
			}
			System.out.println("]");
		}
	}

	public Window getWindow()
	{
		return window;
	}

	public TickEngine getTickEngine()
	{
		return tickEngine;
	}

	public RenderEngine getRenderEngine()
	{
		return renderEngine;
	}

	public InputEngine getInputEngine()
	{
		return inputEngine;
	}

	public AssetManager getAssetManager()
	{
		return this.assetManager;
	}

	public Game getGame()
	{
		return handler;
	}
}
