package net.jimboi.canary.stage_a.pong;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;

/**
 * Created by Andy on 12/1/17.
 */
public class Pong extends Scene
{
	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{

	}

	@Override
	protected void onSceneUpdate()
	{

	}

	@Override
	protected void onSceneDestroy()
	{

	}

	public static Application APPLICATION;
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		APPLICATION = new Application();
		APPLICATION.setFramework(ENGINE = new GameEngine());
		ENGINE.getSceneManager().registerScene("init", Pong.class, PongRenderer.class);
		ENGINE.getSceneManager().setNextScene("init");
		APPLICATION.start();
	}
}
