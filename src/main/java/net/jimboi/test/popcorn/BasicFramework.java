package net.jimboi.test.popcorn;

import org.bstone.application.Application;
import org.bstone.application.Framework;
import org.bstone.input.InputEngine;
import org.bstone.input.context.IAction;
import org.bstone.render.RenderEngine;
import org.bstone.render.RenderHandler;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/11/17.
 */
public class BasicFramework implements Framework, TickHandler, RenderHandler
{
	private TickEngine tickEngine;
	private RenderEngine renderEngine;
	private InputEngine inputEngine;

	private final GameHandler game;

	public BasicFramework(GameHandler game)
	{
		this.game = game;
	}

	@Override
	public void onApplicationStart(Application app)
	{
		this.tickEngine = new TickEngine(60, true, this);
		this.renderEngine = new RenderEngine(this);
		this.inputEngine = new InputEngine(app.getWindow());

		app.startEngine(this.tickEngine);
		app.startEngine(this.renderEngine);
		app.startEngine(this.inputEngine);

		this.inputEngine.getCurrentContext().registerInputMapping("fire",
				this.inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE));

		this.inputEngine.getCurrentContext().addListener(0, context -> {
			IAction action = context.getInputAction("fire");
			if (action != null && action.getState())
			{
				System.out.println("FIRE!");
				context.consumeInput("fire");
			}
		});
	}

	@Override
	public void onApplicationStop(Application app)
	{
	}

	@Override
	public void onFirstUpdate()
	{
		this.game.onFirstUpdate();
	}

	@Override
	public void onEarlyUpdate()
	{
		this.game.onPreUpdate();
	}

	@Override
	public void onFixedUpdate()
	{
		this.game.onUpdate();
	}

	@Override
	public void onLateUpdate()
	{
	}

	@Override
	public void onLastUpdate()
	{
		this.game.onLastUpdate();
	}

	@Override
	public void onRenderLoad()
	{
		this.game.onLoad(this.renderEngine);
	}

	@Override
	public void onRenderUpdate(double delta)
	{
		this.game.onRender(this.renderEngine, delta);
	}

	@Override
	public void onRenderUnload()
	{
		this.game.onUnload(this.renderEngine);
	}
}
