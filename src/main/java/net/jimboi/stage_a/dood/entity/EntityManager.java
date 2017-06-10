package net.jimboi.stage_a.dood.entity;

import org.bstone.util.listener.Listenable;

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
public class EntityManager
{
	private static int NEXT_ENTITY_ID = 0;

	public interface OnEntityAddListener
	{
		void onEntityAdd(Entity entity);
	}

	public interface OnEntityRemoveListener
	{
		void onEntityRemove(Entity entity);
	}

	public final Listenable<OnEntityAddListener> onEntityAdd = new Listenable<>(((listener, objects) -> listener.onEntityAdd((Entity) objects[0])));
	public final Listenable<OnEntityRemoveListener> onEntityRemove = new Listenable<>((listener, objects) -> listener.onEntityRemove((Entity) objects[0]));

	private final Map<Class<? extends Component>, Map<Entity, Component>> components = new HashMap<>();
	private final Map<Integer, Entity> entities = new HashMap<>();
	private final Set<Entity> createSet = new HashSet<>();
	private final Set<Entity> destroySet = new HashSet<>();

	public void update()
	{
		for(Entity entity : this.createSet)
		{
			this.addEntity(entity);
		}
		this.createSet.clear();

		for(Entity entity : this.entities.values())
		{
			if (entity.isDead())
			{
				this.destroySet.add(entity);
			}
		}

		for(Entity entity : this.destroySet)
		{
			this.removeEntity(entity);
		}
		this.destroySet.clear();
	}

	public void clear()
	{
		this.destroySet.addAll(this.entities.values());

		for (Entity entity : this.destroySet)
		{
			this.removeEntity(entity);
		}

		this.destroySet.clear();
		this.createSet.clear();
	}

	public void addComponentToEntity(Entity entity, Component component)
	{
		Class<? extends Component> componentType = component.getClass();
		Map<Entity, Component> map = this.getComponentMap(componentType);
		map.put(entity, component);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponentFromEntity(Entity entity, Class<T> componentType)
	{
		Map<Entity, Component> map = this.getComponentMap(componentType);
		return (T) map.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentByEntity(Class<T> componentType, Entity entity)
	{
		Map<Entity, Component> map = this.getComponentMap(componentType);
		return (T) map.get(entity);
	}

	public boolean hasComponentByEntity(Class<? extends Component> componentType, Entity entity)
	{
		Map<Entity, Component> map = this.getComponentMap(componentType);
		return map.containsKey(entity);
	}

	@SuppressWarnings("unchecked")
	public Entity getEntityByComponent(Component component)
	{
		Class componentType = component.getClass();
		Map<Entity, Component> map = this.getComponentMap(componentType);
		for (Map.Entry<Entity, Component> entry : map.entrySet())
		{
			if (entry.getValue() == component) return entry.getKey();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Entity> getAllEntitiesByComponent(Collection<Entity> dst, Class... componentTypes)
	{
		for (Class componentType : componentTypes)
		{
			if (!componentType.isAssignableFrom(Component.class))
				throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

			Map<Entity, Component> map = this.getComponentMap(componentType);
			dst.addAll(map.keySet());
		}

		return dst;
	}

	@SuppressWarnings("unchecked")
	public Collection<Entity> getEntitiesWithComponent(Class... componentTypes)
	{
		List<Entity> entities = new LinkedList<>();
		boolean init = false;
		for (Class componentType : componentTypes)
		{
			if (!Component.class.isAssignableFrom(componentType))
				throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

			Map<Entity, Component> map = this.getComponentMap(componentType);
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
	public Collection<Entity> retainAllWithComponents(Collection<Entity> dst, Class... componentTypes)
	{
		if (dst.isEmpty())
			throw new UnsupportedOperationException("Unable to retain any entities from an empty collection!");

		for (Class componentType : componentTypes)
		{
			if (!componentType.isAssignableFrom(Component.class))
				throw new IllegalArgumentException("Invalid component type '" + componentType + "'. This is not a component!");

			Map<Entity, Component> map = this.getComponentMap(componentType);
			dst.retainAll(map.keySet());
		}

		return dst;
	}

	private Map<Entity, Component> getComponentMap(Class<? extends Component> componentType)
	{
		Map<Entity, Component> map = this.components.get(componentType);
		if (map == null)
		{
			map = new HashMap<>();
			this.components.put(componentType, map);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public <T extends Entity> T getEntityByID(int id)
	{
		return (T) this.entities.get(id);
	}

	public Entity createEntity()
	{
		Entity entity = new Entity(this);
		this.createSet.add(entity);
		return entity;
	}

	public Entity createEntityWithComponents(Component... components)
	{
		Entity entity = this.createEntity();
		for(Component component : components)
		{
			this.addComponentToEntity(entity, component);
		}
		return entity;
	}

	protected  <T extends Entity> T addEntity(T entity)
	{
		entity.id = this.getNextAvailableEntityID();
		this.entities.put(entity.getEntityID(), entity);

		this.onEntityAdd.notifyListeners(entity);
		return entity;
	}

	protected <T extends Entity> T removeEntity(T entity)
	{
		Entity e = this.entities.remove(entity.getEntityID());

		if (e == null)
			throw new IllegalArgumentException("Entity does not exist with id '" + entity.getEntityID() + "'!");

		for(Map<Entity, Component> map : this.components.values())
		{
			map.remove(entity);
		}

		this.onEntityRemove.notifyListeners(entity);
		return entity;
	}

	protected int getNextAvailableEntityID()
	{
		return NEXT_ENTITY_ID++;
	}
}
