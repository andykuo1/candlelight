package net.jimboi.dood;

import net.jimboi.base.Main;
import net.jimboi.mod.renderer.Renderer;
import net.jimboi.mod.scene.EntitySceneBase;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

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
	protected final void onSceneLoad()
	{
		Main.RENDERENGINE.add(this.renderer);
	}

	@Override
	protected final void onSceneUnload()
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
