package org.bstone.game;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderHandler;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
import org.bstone.window.Window;
import org.bstone.window.input.InputManager;

/**
 * Created by Andy on 8/5/17.
 */
public class GameEngine implements TickHandler, RenderHandler
{
	private final TickEngine tickEngine;
	private final RenderEngine renderEngine;

	private Window window;
	private InputManager inputManager;

	private final GameHandler handler;

	public GameEngine(GameHandler handler)
	{
		this.handler = handler;
		this.tickEngine = new TickEngine(60, true, this);
		this.renderEngine = new RenderEngine(this);
	}

	public void start()
	{
		this.tickEngine.run();
	}

	public void stop()
	{
		this.tickEngine.stop();
	}

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.window = new Window(this.handler.getClass().getSimpleName(), 640, 480);
		this.inputManager = new InputManager(this.window.getInputEngine());

		this.renderEngine.load();

		this.handler.onFirstUpdate();
	}

	@Override
	public void onPreUpdate()
	{
		this.window.poll();
		this.handler.onPreUpdate();

		if (this.window.shouldCloseWindow() && this.handler.onWindowClose())
		{
			this.stop();
		}
	}

	@Override
	public void onFixedUpdate()
	{
		this.window.updateInput();
		this.handler.onUpdate();
	}

	@Override
	public void onUpdate(double delta)
	{
		this.window.clearScreenBuffer();
		{
			this.renderEngine.update(delta);
		}
		this.window.updateScreenBuffer();
	}

	@Override
	public void onLastUpdate(TickEngine tickEngine)
	{
		this.handler.onLastUpdate();

		this.renderEngine.unload();

		this.inputManager.clear();
		this.window.destroy();
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		this.handler.onLoad(renderEngine);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.handler.onRender(renderEngine, delta);
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.handler.onUnload(renderEngine);
	}

	public TickEngine getTickEngine()
	{
		return this.tickEngine;
	}

	public RenderEngine getRenderEngine()
	{
		return this.renderEngine;
	}

	public Window getWindow()
	{
		return this.window;
	}

	public InputManager getInput()
	{
		return this.inputManager;
	}
}
