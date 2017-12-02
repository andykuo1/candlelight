package org.bstone.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 8/12/17.
 */
public class EntityManager
{
	protected final Map<Class<? extends Component>, Map<Entity, Component>> components = new HashMap<>();
	protected final Map<Integer, Entity> entities = new HashMap<>();

	private int nextEntityID = 0;

	public <T extends Entity> T addEntity(T entity, Component... components)
	{
		entity.id = this.getNextAvailableEntityID();
		entity.entityManager = this;
		this.entities.put(entity.getEntityID(), entity);

		for(Component component : components)
		{
			this.addComponentToEntity(entity, component);
		}

		entity.onEntitySetup(this);
		this.onEntityAdd(entity);

		return entity;
	}

	public Entity removeEntity(Entity entity)
	{
		if (entity.getEntityManager() != this) throw new IllegalArgumentException("Cannot remove entity that the manager does not own!");
		if (entity.getEntityID() == -1) throw new IllegalArgumentException("Cannot remove entity not yet initialized!");

		entity.onEntityDelete();
		this.onEntityRemove(entity);

		this.clearComponentFromEntity(entity);

		Entity result = this.entities.remove(entity.getEntityID());
		if (result == null) throw new IllegalArgumentException("Cannot find entity '" + entity.getClass().getSimpleName() + "' with id '" + entity.getEntityID() + "' to remove!");

		entity.entityManager = null;
		entity.id = -1;

		return result;
	}

	public void clear()
	{
		this.components.clear();
		this.entities.clear();
	}

	public boolean addComponentToEntity(Entity entity, Component component)
	{
		if (component == null) return false;

		Map<Entity, Component> entityComponentMap = this.components.computeIfAbsent(component.getClass(), key -> new HashMap<>());
		Component result = entityComponentMap.put(entity, component);

		if (result != null) throw new IllegalArgumentException("Cannot add component '" + component.getClass().getSimpleName() + "', since there exists another component of the same type in the same entity!");

		this.onComponentAdd(entity, component);

		return true;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponentFromEntity(Entity entity, Class<T> type)
	{
		Map<Entity, Component> entityComponentMap = this.components.get(type);
		if (entityComponentMap == null) return null;

		T result = (T) entityComponentMap.remove(entity);
		this.onComponentRemove(entity, result);

		return result;
	}

	public void clearComponentFromEntity(Entity entity)
	{
		for(Map<Entity, Component> entityComponentMap : this.components.values())
		{
			Component component = entityComponentMap.remove(entity);
			this.onComponentRemove(entity, component);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentFromEntity(Entity entity, Class<? extends T> type)
	{
		Map<Entity, Component> entityComponentMap = this.components.get(type);

		if (entityComponentMap == null)
		{
			for (Class<? extends Component> componentType : this.components.keySet())
			{
				if (type.isAssignableFrom(componentType))
				{
					Component component = this.components.get(componentType).get(entity);
					if (component != null)
					{
						return (T) component;
					}
				}
			}

			return null;
		}

		return (T) entityComponentMap.get(entity);
	}

	public Entity getEntityByComponent(Component component)
	{
		Map<Entity, Component> entityComponentMap = this.components.get(component.getClass());
		if (entityComponentMap == null) return null;
		for(Map.Entry<Entity, Component> entry : entityComponentMap.entrySet())
		{
			if (entry.getValue() == component) return entry.getKey();
		}
		return null;
	}

	public Entity getEntityByID(int id)
	{
		return this.entities.get(id);
	}

	public int getNextAvailableEntityID()
	{
		return this.nextEntityID++;
	}

	/**
	 * Gets an iterable backed by the map
	 */
	public Iterable<Entity> getEntities()
	{
		return this.entities.values();
	}

	//TODO: This implies that there exists a type such that T is a valid instance type
	@SuppressWarnings("unchecked")
	public <T, E extends Component> Collection<T> getComponents(Class<? extends E> type, Collection<T> dst)
	{
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			if (type.isAssignableFrom(componentType))
			{
				Map<Entity, Component> entityComponentMap = this.components.get(componentType);
				for(Component component : entityComponentMap.values())
				{
					dst.add((T) component);
				}
			}
		}
		return dst;
	}

	public Collection<Entity> getEntitiesWith(Class<? extends Component>[] types, Collection<Entity> dst)
	{
		boolean flag = false;
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			for(Class<? extends Component> type : types)
			{
				if (type.isAssignableFrom(componentType))
				{
					Map<Entity, Component> entityComponentMap = this.components.get(componentType);
					if (!flag)
					{
						dst.addAll(entityComponentMap.keySet());
						flag = true;
					}
					else
					{
						dst.retainAll(entityComponentMap.keySet());

						if (dst.isEmpty())
						{
							return dst;
						}
					}
				}
			}
		}
		return dst;
	}

	public Collection<Entity> getEntitiesWithAny(Class<? extends Component>[] types, Collection<Entity> dst)
	{
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			for(Class<? extends Component> type : types)
			{
				if (type.isAssignableFrom(componentType))
				{
					Map<Entity, Component> entityComponentMap = this.components.get(componentType);
					dst.addAll(entityComponentMap.keySet());
				}
			}
		}
		return dst;
	}

	protected void onEntityAdd(Entity entity)
	{
	}

	protected void onEntityRemove(Entity entity)
	{
	}

	protected void onComponentAdd(Entity entity, Component component)
	{
	}

	protected void onComponentRemove(Entity entity, Component component)
	{
	}
}
