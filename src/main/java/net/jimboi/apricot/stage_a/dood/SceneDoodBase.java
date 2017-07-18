package net.jimboi.apricot.stage_a.dood;

import net.jimboi.apricot.stage_a.dood.entity.Entity;
import net.jimboi.apricot.stage_a.dood.entity.EntityManager;
import net.jimboi.apricot.stage_a.mod.renderer.OldRenderService;
import net.jimboi.apricot.stage_a.mod.scene.EntitySceneBase;

import org.qsilver.render.RenderEngine;

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
	protected final void onSceneLoad(RenderEngine renderEngine)
	{
		renderEngine.startService(this.renderer);
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderEngine)
	{
		renderEngine.stopService(this.renderer);
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
