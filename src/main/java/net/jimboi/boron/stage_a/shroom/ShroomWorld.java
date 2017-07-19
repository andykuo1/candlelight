package net.jimboi.boron.stage_a.shroom;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class ShroomWorld
{
	protected final SceneShroomBase<? extends RenderShroomBase> scene;

	public ShroomWorld(SceneShroomBase<? extends RenderShroomBase> scene)
	{
		this.scene = scene;
	}

	public abstract void create();

	public abstract void update(double delta);

	public abstract void destroy();

	public SceneShroomBase<? extends RenderShroomBase> getScene()
	{
		return this.scene;
	}
}
