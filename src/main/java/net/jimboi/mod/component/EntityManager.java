package net.jimboi.mod.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 5/21/17.
 */
public class EntityManager
{
	private static int NEXT_ENTITY_ID = 0;

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

	public void addComponentToEntity(Entity entity, Component component)
	{
		Class<? extends Component> componentType = component.getClass();
		Map<Entity, Component> map = this.getComponents(componentType);
		map.put(entity, component);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponentFromEntity(Entity entity, Class<T> componentType)
	{
		Map<Entity, Component> map = this.getComponents(componentType);
		return (T) map.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentByEntity(Class<T> componentType, Entity entity)
	{
		Map<Entity, ? extends Component> map = this.getComponents(componentType);
		return (T) map.get(entity);
	}

	public boolean hasComponentByEntity(Class<? extends Component> componentType, Entity entity)
	{
		Map<Entity, Component> map = this.getComponents(componentType);
		return map.containsKey(entity);
	}

	public Collection<Entity> getEntitiesByComponent(Collection<Entity> dst, Class<? extends Component>... componentTypes)
	{
		boolean init = false;
		for(Class<? extends Component> componentType : componentTypes)
		{
			Map<Entity, ? extends Component> map = this.getComponents(componentType);
			if (init)
			{
				dst.retainAll(map.keySet());
			}
			else
			{
				dst.addAll(map.keySet());
				init = true;
			}
		}

		return dst;
	}

	private Map<Entity, Component> getComponents(Class<? extends Component> componentType)
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
		return new Entity(this);
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
		return entity;
	}

	protected <T extends Entity> T removeEntity(T entity)
	{
		this.entities.remove(entity.getEntityID());
		for(Map<Entity, Component> map : this.components.values())
		{
			map.remove(entity);
		}
		return entity;
	}

	protected int getNextAvailableEntityID()
	{
		return NEXT_ENTITY_ID++;
	}
}
