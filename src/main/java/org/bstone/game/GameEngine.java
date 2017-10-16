package org.bstone.game;

import net.jimboi.boron.base.render.OldRenderEngine;
import net.jimboi.boron.base.render.OldRenderHandler;
import net.jimboi.boron.base.tick.OldTickEngine;
import net.jimboi.boron.base.tick.OldTickHandler;
import net.jimboi.boron.base.window.OldWindow;
import net.jimboi.boron.base.window.input.InputManager;

/**
 * Created by Andy on 8/5/17.
 */
public class GameEngine implements OldTickHandler, OldRenderHandler
{
	private final OldTickEngine tickEngine;
	private final OldRenderEngine renderEngine;

	private OldWindow window;
	private InputManager inputManager;

	private final GameHandler handler;

	public GameEngine(GameHandler handler)
	{
		this.handler = handler;
		this.tickEngine = new OldTickEngine(60, true, this);
		this.renderEngine = new OldRenderEngine(this);
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
	public void onFirstUpdate(OldTickEngine tickEngine)
	{
		this.window = new OldWindow(this.handler.getClass().getSimpleName(), 640, 480);
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
	public void onLastUpdate(OldTickEngine tickEngine)
	{
		this.handler.onLastUpdate();

		this.renderEngine.unload();

		this.inputManager.clear();
		this.window.destroy();

		this.renderEngine.destroy();
	}

	@Override
	public void onRenderLoad(OldRenderEngine renderEngine)
	{
		this.handler.onLoad(renderEngine);
	}

	@Override
	public void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{
		this.handler.onRender(renderEngine, delta);
	}

	@Override
	public void onRenderUnload(OldRenderEngine renderEngine)
	{
		this.handler.onUnload(renderEngine);
	}

	public OldTickEngine getTickEngine()
	{
		return this.tickEngine;
	}

	public OldRenderEngine getRenderEngine()
	{
		return this.renderEngine;
	}

	public OldWindow getWindow()
	{
		return this.window;
	}

	public InputManager getInput()
	{
		return this.inputManager;
	}
}
