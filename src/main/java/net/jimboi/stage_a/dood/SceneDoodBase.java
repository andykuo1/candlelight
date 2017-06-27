package net.jimboi.stage_a.dood;

import net.jimboi.base.Main;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;
import net.jimboi.stage_a.mod.scene.EntitySceneBase;

import org.qsilver.renderer.RenderEngine;
import org.qsilver.renderer.Renderer;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class SceneDoodBase extends EntitySceneBase implements EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
{
	protected final Renderer renderer;

	public SceneDoodBase(Renderer renderer)
	{
		this.renderer = renderer;
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderManager)
	{
		Main.RENDERENGINE.add(this.renderer);
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderManager)
	{
		Main.RENDERENGINE.remove(this.renderer);
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
