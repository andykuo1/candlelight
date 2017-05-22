package net.jimboi.dood;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Render;
import org.qsilver.render.RenderManager;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class SceneBase extends Scene implements EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener, InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	protected final EntityManager entityManager;
	protected final RenderManager renderManager;
	protected final InstanceManager instanceManager;

	public SceneBase()
	{
		this.entityManager = new EntityManager();
		this.entityManager.onEntityAdd.addListener(this);
		this.entityManager.onEntityRemove.addListener(this);

		this.renderManager = new RenderManager();

		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getMaterial().getRenderID());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);
	}

	@Override
	protected void onUpdate(double delta)
	{
		this.entityManager.update();
	}

	@Override
	protected void onRender()
	{
		this.instanceManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	@Override
	protected void onSceneUnload()
	{
		this.instanceManager.destroyAll();
	}

	@Override
	public void onInstanceAdd(InstanceHandler parent, Instance instance)
	{
	}

	@Override
	public void onInstanceRemove(InstanceHandler parent, Instance instance)
	{
	}

	@Override
	public void onEntityAdd(Entity entity)
	{
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}
}
