package apricot.stage_a.dood;

import apricot.stage_a.dood.entity.Entity;
import apricot.stage_a.dood.entity.EntityManager;
import apricot.stage_a.mod.renderer.OldRenderService;
import apricot.stage_a.mod.scene.EntitySceneBase;
import apricot.base.render.OldRenderEngine;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class SceneDoodBase extends EntitySceneBase implements EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
{
	protected final OldRenderService renderer;

	public SceneDoodBase(OldRenderService renderer)
	{
		this.renderer = renderer;
	}

	@Override
	protected final void onSceneLoad(OldRenderEngine renderEngine)
	{
		this.renderer.start();
	}

	@Override
	protected final void onSceneUnload(OldRenderEngine renderEngine)
	{
		this.renderer.stop();
	}

	@Override
	public void onEntityAdd(Entity entity)
	{
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
	}
}
