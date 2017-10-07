package net.jimboi.boron.stage_c.shooter;

import org.bstone.game.GameEngine;
import org.bstone.game.GameHandler;
import org.bstone.render.RenderEngine;

/**
 * Created by Andy on 10/6/17.
 */
public class ShooterGame implements GameHandler
{
	private static ShooterGame INSTANCE;
	private static GameEngine ENGINE;

	public static ShooterGame getGame()
	{
		return INSTANCE;
	}

	public static GameEngine getEngine()
	{
		return ENGINE;
	}

	private ShooterGame()
	{}

	public static void main(String[] args)
	{
		INSTANCE = new ShooterGame();
		ENGINE = new GameEngine(INSTANCE);
		ENGINE.start();
	}

	@Override
	public void onFirstUpdate()
	{
	}

	@Override
	public void onPreUpdate()
	{

	}

	@Override
	public void onUpdate()
	{

	}

	@Override
	public void onLastUpdate()
	{

	}

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
	}

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
	{

	}

	@Override
	public void onUnload(RenderEngine renderEngine)
	{

	}
}
