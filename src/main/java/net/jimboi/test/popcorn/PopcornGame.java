package net.jimboi.test.popcorn;

import org.bstone.application.Application;
import org.bstone.render.RenderEngine;

/**
 * Created by Andy on 10/12/17.
 */
public class PopcornGame implements GameHandler
{
	public static void main(String[] args)
	{
		PopcornGame game = new PopcornGame();
		BasicFramework framework = new BasicFramework(game);
		Application app = new Application(framework);
		app.run();
	}

	@Override
	public void onFirstUpdate()
	{
		System.out.println("FIRST UPDATE!");
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
		System.out.println("LAST UPDATE!");
	}

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
		System.out.println("LOAD!");
	}

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
	{

	}

	@Override
	public void onUnload(RenderEngine renderEngine)
	{
		System.out.println("UNLOAD!");
	}
}
