package org.bstone.application.game;

import org.bstone.application.Application;
import org.bstone.application.Framework;
import org.bstone.input.InputEngine;
import org.bstone.input.context.InputContext;
import org.bstone.input.context.InputMapping;
import org.bstone.render.RenderEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;

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

	private double timeCounter;

	public GameEngine(Game handler)
	{
		this.handler = handler;

		this.window = new Window();
		this.inputEngine = new InputEngine(this.window);
		this.tickEngine = new TickEngine(60, true, this.handler);
		this.renderEngine = new RenderEngine(this.window, this.tickEngine, this.handler);
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
		this.input = this.inputEngine.createContext(0);
		this.input.addListener(0, this.handler);

		app.startEngine(this.inputEngine);
		app.startEngine(this.renderEngine);
		app.startEngine(this.tickEngine);

		this.timeCounter = System.currentTimeMillis();
	}

	@Override
	public void onApplicationDestroy(Application app)
	{
		Window.terminateGLFW();
	}

	@Override
	public void onApplicationUpdate(Application app)
	{
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

	public InputContext getInputContext()
	{
		return this.input;
	}

	public InputMapping getInput()
	{
		return this.inputEngine.getInput();
	}

	public Game getGame()
	{
		return handler;
	}
}
