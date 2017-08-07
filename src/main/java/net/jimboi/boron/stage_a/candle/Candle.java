package net.jimboi.boron.stage_a.candle;

import org.bstone.game.GameEngine;
import org.bstone.game.GameHandler;
import org.bstone.render.RenderEngine;
import org.qsilver.asset.AssetManager;
import org.qsilver.poma.Poma;
import org.qsilver.scene.SceneManager;

import java.util.Arrays;

/**
 * Created by Andy on 7/29/17.
 */
public class Candle implements GameHandler
{
	static
	{
		Poma.makeSystemLogger();
	}

	public static Candle getCandle()
	{
		return INSTANCE;
	}

	private static Candle INSTANCE;
	private static GameEngine GAMEENGINE;

	private Candle() {}

	public static void main(String[] args)
	{
		Poma.div();
		Poma.info("The Great Engine");
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		INSTANCE = new Candle();
		GAMEENGINE = new GameEngine(INSTANCE);
		GAMEENGINE.start();
	}

	public SceneManager sceneManager;
	public AssetManager assetManager;

	@Override
	public void onFirstUpdate()
	{
		this.sceneManager = new SceneManager();
		this.sceneManager.nextScene(new SceneCandle());
	}

	@Override
	public void onPreUpdate()
	{
	}

	@Override
	public void onUpdate()
	{
		this.sceneManager.update(1);

		if (!this.sceneManager.isActive())
		{
			GAMEENGINE.stop();
		}
	}

	@Override
	public void onLastUpdate()
	{
	}

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
		this.assetManager = new AssetManager();
	}

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
	{
		this.sceneManager.renderUpdate(renderEngine);
		this.assetManager.update(renderEngine);
	}

	@Override
	public void onUnload(RenderEngine renderEngine)
	{
		this.assetManager.destroy();
	}

	private boolean closing = false;

	@Override
	public boolean onWindowClose()
	{
		if (!this.closing)
		{
			this.closing = true;

			this.sceneManager.stopScene();
		}
		return false;
	}

	public AssetManager getAssetManager()
	{
		return this.assetManager;
	}

	public SceneManager getSceneManager()
	{
		return this.sceneManager;
	}
}
