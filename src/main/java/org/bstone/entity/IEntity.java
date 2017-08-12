package org.bstone.entity;

/**
 * Created by Andy on 8/12/17.
 */
public interface IEntity
{
	void onEntityCreate(EntityManager entityManager);
	void onEntityDestroy();

	@SuppressWarnings("unchecked")
	default void addComponent(Component component)
	{
		this.getEntityManager().addComponentToEntity(this, component);
	}

	@SuppressWarnings("unchecked")
	default <T extends Component> T removeComponent(Class<T> componentType)
	{
		return (T) this.getEntityManager().removeComponentFromEntity(this, componentType);
	}

	@SuppressWarnings("unchecked")
	default <T extends Component> T getComponent(Class<T> componentType)
	{
		return (T) this.getEntityManager().getComponentFromEntity(this, componentType);
	}

	void setEntityManager(EntityManager entityManager);
	void setEntityID(int id);

	EntityManager getEntityManager();
	int getEntityID();
}
