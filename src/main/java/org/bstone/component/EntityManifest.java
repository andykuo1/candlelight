package org.bstone.component;

/**
 * Created by Andy on 5/21/17.
 */
public class EntityManifest<C extends Component>
{
	ComponentManager componentManager = null;
	int id = -1;

	@SuppressWarnings("unchecked")
	protected EntityManifest addComponent(C component)
	{
		this.componentManager.addComponentToEntity(this, component);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected <T extends C> T removeComponent(Class<T> componentType)
	{
		return (T) this.componentManager.removeComponentFromEntity(this, componentType);
	}

	@SuppressWarnings("unchecked")
	public final <T extends C> T getComponent(Class<T> componentType)
	{
		return (T) this.componentManager.getComponentByEntity(componentType, this);
	}

	@SuppressWarnings("unchecked")
	public final boolean hasComponent(Class<? extends C> componentType)
	{
		return this.componentManager.hasComponentByEntity(componentType, this);
	}

	public final int getEntityID()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof EntityManifest)
		{
			return this.componentManager == ((EntityManifest) o).componentManager && this.id == ((EntityManifest) o).id;
		}
		return false;
	}
}
