package org.bstone.entity;

/**
 * Created by Andy on 12/1/17.
 */
public class Entity
{
	EntityManager entityManager;
	int id;

	protected void onEntitySetup(EntityManager entityManager)
	{
	}

	protected void onEntityDelete()
	{
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T addComponent(T component)
	{
		this.entityManager.addComponentToEntity(this, component);
		return component;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponent(Class<T> componentType)
	{
		return this.entityManager.removeComponentFromEntity(this, componentType);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> componentType)
	{
		return this.entityManager.getComponentFromEntity(this, componentType);
	}

	public final EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public final int getEntityID()
	{
		return this.id;
	}
}
