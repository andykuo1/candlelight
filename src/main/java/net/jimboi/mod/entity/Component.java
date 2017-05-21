package net.jimboi.mod.entity;

/**
 * Created by Andy on 4/30/17.
 */
public class Component
{
	protected final Entity entity;

	public Component(Entity entity)
	{
		this.entity = entity;
	}

	public void onComponentSetup(ComponentHandler componentHandler)
	{
	}

	public boolean onEntityCreate(Entity entity)
	{
		return true;
	}

	public void onEntityUpdate(double delta)
	{
	}

	public void onEntityDestroy()
	{
	}

	public Entity getEntity()
	{
		return this.entity;
	}
}
