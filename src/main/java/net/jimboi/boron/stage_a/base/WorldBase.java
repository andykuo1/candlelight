package net.jimboi.boron.stage_a.base;

import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneService;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class WorldBase extends SceneService
{
	private Scene scene;

	public WorldBase(Scene scene)
	{
		super(scene);

		this.scene = scene;
	}

	@Override
	protected void onServiceStart(Scene handler)
	{
		this.create();
	}

	@Override
	protected void onServiceStop(Scene handler)
	{
		this.destroy();
	}

	@Override
	protected void onSceneUpdate(Scene scene)
	{

	}

	protected abstract void create();

	protected abstract void destroy();

	public Scene getScene()
	{
		return this.scene;
	}
}
