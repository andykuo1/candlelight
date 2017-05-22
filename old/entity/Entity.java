package org.qsilver.entity;

import net.jimboi.mod.transform.Transform;
import net.jimboi.mod3.blob.RenderUtil;

import org.qsilver.living.Living;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class Entity extends Living implements InstanceHandler
{
	protected final ComponentHandler componentHandler = new ComponentHandler();
	private final Transform transform;

	protected Entity(Transform transform)
	{
		this.transform = transform;

		this.onComponentSetup(this.componentHandler);
		this.componentHandler.setupComponents();
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RenderUtil.getModel(this.getModelID());
		Material material = RenderUtil.getMaterial(this.getMaterialID());
		instances.add(new Instance(model, material));
	}

	@Override
	public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
	{
		if (this.isDead())
		{
			instance.setDead();
			return;
		}

		instance.setTransformation(this.transform);
	}

	@Override
	public void onInstanceDestroy(InstanceManager instanceManager, Instance instance)
	{
	}

	public abstract void onComponentSetup(ComponentHandler componentHandler);

	@Override
	public boolean onCreate()
	{
		Iterator<Component> components = this.componentHandler.getComponents();
		while(components.hasNext())
		{
			Component component = components.next();
			if (!component.onEntityCreate(this))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public void onEarlyUpdate()
	{
		super.onEarlyUpdate();
	}

	@Override
	public void onUpdate(double delta)
	{
		super.onUpdate(delta);

		Iterator<Component> components = this.componentHandler.getComponents();
		while(components.hasNext())
		{
			Component component = components.next();
			component.onEntityUpdate(delta);
		}
	}

	@Override
	public void onLateUpdate()
	{
		super.onLateUpdate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		Iterator<Component> components = this.componentHandler.getComponents();
		while(components.hasNext())
		{
			Component component = components.next();
			component.onEntityDestroy();
		}
	}

	public abstract String getModelID();
	public abstract String getMaterialID();

	public final ComponentHandler getComponentHandler()
	{
		return this.componentHandler;
	}

	public Transform transform()
	{
		return this.transform;
	}
}
