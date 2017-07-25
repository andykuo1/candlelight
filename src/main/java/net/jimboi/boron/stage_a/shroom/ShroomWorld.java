package net.jimboi.boron.stage_a.shroom;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class ShroomWorld
{
	protected final SceneShroomBase scene;

	public ShroomWorld(SceneShroomBase scene)
	{
		this.scene = scene;
	}

	public abstract void create();

	public abstract void update(double delta);

	public abstract void destroy();

	public SceneShroomBase getScene()
	{
		return this.scene;
	}
}
