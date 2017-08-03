package net.jimboi.boron.base;

import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneService;

/**
 * Created by Andy on 7/29/17.
 */
public abstract class WorldBase extends SceneService
{
	private final Scene scene;

	public WorldBase(Scene scene)
	{
		this.scene = scene;
	}

	@Override
	protected void onStart(Scene handler)
	{
		this.setupWorld();
	}

	@Override
	protected void onStop(Scene handler)
	{

	}

	protected abstract void setupWorld();

	public Scene getScene()
	{
		return this.scene;
	}
}
