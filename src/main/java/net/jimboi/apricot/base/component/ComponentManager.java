package net.jimboi.apricot.base.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 5/21/17.
 */
@Deprecated
public class ComponentManager<E extends ManifestEntity, C extends OldComponent>
{
	private static int NEXT_ENTITY_ID = 0;

	protected final Map<Class<? extends C>, Map<E, C>> components = new HashMap<>();
	protected final Map<Integer, E> entities = new HashMap<>();

	private final Class<C> baseComponent;

	public ComponentManager(Class<C> componentType)
	{
		this.baseComponent = componentType;
	}

	protected void onComponentAdd(E entity, C component)
	{}

	protected void onComponentRemove(E entity, C component)
	{}

	public void clear()
	{
		for (E entity : this.entities.values())
		{
			this.removeAllComponentsFromEntity(entity);
		}

		this.components.clear();
		this.entities.clear();
	}

	@SuppressWarnings("unchecked")
	public boolean addComponentToEntity(E entity, C component)
	{
		if (component == null) return false;

		Class componentType = component.getClass();
		Map<E, C> map = (Map<E, C>) this.getComponentMap(componentType);
		map.put(entity, component);
		this.onComponentAdd(entity, component);
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T extends C> T removeComponentFromEntity(E entity, Class<T> componentType)
	{
		Map<E, C> map = this.getComponentMap(componentType);
		T t = (T) map.remove(entity);
		this.onComponentRemove(entity, t);
		return t;
	}

	public void removeAllComponentsFromEntity(E entity)
	{
		for (Map<E, C> map : this.components.values())
		{
			C c = map.remove(entity);
			this.onComponentRemove(entity, c);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends C> T getComponentByEntity(Class<T> componentType, E entity)
	{
		Map<E, C> map = this.getComponentMap(componentType);
		return (T) map.get(entity);
	}

	public boolean hasComponentByEntity(Class<? extends C> componentType, E entity)
	{
		Map<E, C> map = this.getComponentMap(componentType);
		return map.containsKey(entity);
	}

	@SuppressWarnings("unchecked")
	public E getEntityByComponent(C component)
	{
		Class componentType = component.getClass();
		Map<E, C> map = this.getComponentMap(componentType);
		for (Map.Entry<E, C> entry : map.entrySet())
		{
			if (entry.getValue() == component) return entry.getKey();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<E> getEntitiesWithComponent(Class... componentTypes)
	{
		List<E> entities = new LinkedList<>();
		boolean init = false;
		for (Class componentType : componentTypes)
		{
			if (!this.baseComponent.isAssignableFrom(componentType))
				throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

			Map<E, C> map = this.getComponentMap(componentType);
			if (init)
			{
				entities.retainAll(map.keySet());
			}
			else
			{
				entities.addAll(map.keySet());
				init = true;
			}
		}

		return entities;
	}

	@SuppressWarnings("unchecked")
	public <D extends C> Collection<D> getComponents(Class<D> componentType, Collection<D> dst)
	{
		dst.addAll(((Map<E, D>) this.getComponentMap(componentType)).values());
		return dst;
	}

	@SuppressWarnings("unchecked")
	public <D extends C> Collection<D> getSimilarComponents(Class<D> componentType, Collection<D> dst)
	{
		this.getSimilarComponentEntries(componentType, _ENTRIES);
		for(Map.Entry<Class<? extends C>, Map<E, C>> entry : _ENTRIES)
		{
			Collection<C> col = entry.getValue().values();
			for(C c : col)
			{
				dst.add((D) c);
			}
		}
		_ENTRIES.clear();
		return dst;
	}

	@SuppressWarnings("unchecked")
	public Collection<E> addAllWithComponent(Class componentType, Collection<E> dst)
	{
		if (!componentType.isAssignableFrom(this.baseComponent))
			throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

		Map<E, C> map = this.getComponentMap(componentType);
		dst.addAll(map.keySet());

		return dst;
	}

	@SuppressWarnings("unchecked")
	public Collection<E> retainAllWithComponents(Class[] componentTypes, Collection<E> dst)
	{
		if (dst.isEmpty())
			throw new UnsupportedOperationException("Unable to retain any entities from an empty collection!");

		for (Class componentType : componentTypes)
		{
			if (!componentType.isAssignableFrom(this.baseComponent))
				throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

			Map<E, C> map = this.getComponentMap(componentType);
			dst.retainAll(map.keySet());
		}

		return dst;
	}

	private final Set<Map.Entry<Class<? extends C>, Map<E, C>>> _ENTRIES = new HashSet<>();

	protected void getSimilarComponentEntries(Class<? extends C> componentType, Set<Map.Entry<Class<? extends C>, Map<E, C>>> dst)
	{
		for(Map.Entry<Class<? extends C>, Map<E, C>> entry : this.components.entrySet())
		{
			if(componentType.isAssignableFrom(entry.getKey()))
			{
				dst.add(entry);
			}
		}
	}

	protected Map<E, C> getComponentMap(Class<? extends C> componentType)
	{
		return this.components.computeIfAbsent(componentType, (key) -> new HashMap<>());
	}

	@SuppressWarnings("unchecked")
	public <T extends E> T getEntityByID(int id)
	{
		return (T) this.entities.get(id);
	}

	protected <T extends E> T addEntity(T entity, C... components)
	{
		entity.componentManager = this;
		entity.id = this.getNextAvailableEntityID();

		this.entities.put(entity.id, entity);

		for (C component : components)
		{
			if (component == null) continue;

			this.addComponentToEntity(entity, component);
		}

		return entity;
	}

	protected <T extends E> T removeEntity(T entity)
	{
		if (this.entities.containsValue(entity))
		{
			this.entities.remove(entity.id);
		}
		else if (entity.componentManager == this)
		{
			//Already removed, but not processed
		}
		else
		{
			throw new IllegalArgumentException("Entity does not exist with id '" + entity.getEntityID() + "'!");
		}

		entity.componentManager = null;
		entity.id = -1;

		this.removeAllComponentsFromEntity(entity);

		return entity;
	}

	protected int getNextAvailableEntityID()
	{
		return NEXT_ENTITY_ID++;
	}
}
