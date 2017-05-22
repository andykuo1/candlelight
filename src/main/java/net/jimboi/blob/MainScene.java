package net.jimboi.blob;

import net.jimboi.blob.livings.LivingBase;

import org.qsilver.living.Living;
import org.qsilver.living.LivingManager;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Render;
import org.qsilver.render.RenderManager;
import org.qsilver.scene.Scene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class MainScene extends Scene implements LivingManager.OnLivingAddListener, LivingManager.OnLivingRemoveListener, InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	protected final LivingManager livingManager;
	protected final RenderManager renderManager;
	protected final InstanceManager instanceManager;

	protected final Set<LivingBase> entities = new HashSet<>();

	public MainScene()
	{
		this.livingManager = new LivingManager();
		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);

		this.renderManager = new RenderManager();
		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getMaterial().getRenderID());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);

		this.livingManager.addLivingSet(this.entities, LivingBase.class);
	}

	@Override
	public void onUpdate(double delta)
	{
		this.livingManager.update(delta);
	}

	@Override
	public void onRender()
	{
		this.instanceManager.update();
	}

	@Override
	public void onSceneStop()
	{
		this.livingManager.destroyAll();
	}

	@Override
	public void onSceneUnload()
	{
		this.instanceManager.destroyAll();
	}

	@Override
	public void onLivingAdd(Living living)
	{
		if (living instanceof InstanceHandler)
		{
			this.instanceManager.add((InstanceHandler) living);
		}
	}

	@Override
	public void onLivingRemove(Living living)
	{
	}

	@Override
	public void onInstanceAdd(InstanceHandler parent, Instance instance)
	{
	}

	@Override
	public void onInstanceRemove(InstanceHandler parent, Instance instance)
	{
	}

	public LivingManager getLivingManager()
	{
		return this.livingManager;
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}

	public Iterator<LivingBase> getEntities()
	{
		return this.entities.iterator();
	}
}
