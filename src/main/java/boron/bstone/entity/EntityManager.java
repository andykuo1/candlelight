package boron.bstone.entity;

import boron.bstone.util.listener.Listenable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 8/12/17.
 */
public class EntityManager<E extends IEntity>
{
	public interface OnEntityCreateListener<E extends IEntity>
	{
		void onEntityCreate(E entity);
	}

	public interface OnEntityDestroyListener<E extends IEntity>
	{
		void onEntityDestroy(E entity);
	}

	public interface OnEntityComponentAddListener<E extends IEntity>
	{
		void onEntityComponentAdd(E entity, Component component);
	}

	public interface OnEntityComponentRemoveListener<E extends IEntity>
	{
		void onEntityComponentRemove(E entity, Component component);
	}

	@SuppressWarnings("unchecked")
	public final Listenable<OnEntityCreateListener<E>> onEntityCreate
			= new Listenable<>((listener, objects) -> listener.onEntityCreate((E) objects[0]));
	@SuppressWarnings("unchecked")
	public final Listenable<OnEntityDestroyListener<E>> onEntityDestroy
			= new Listenable<>(((listener, objects) -> listener.onEntityDestroy((E) objects[0])));
	@SuppressWarnings("unchecked")
	public final Listenable<OnEntityComponentAddListener<E>> onEntityComponentAdd
			= new Listenable<>(((listener, objects) -> listener.onEntityComponentAdd((E) objects[0], (Component) objects[1])));
	@SuppressWarnings("unchecked")
	public final Listenable<OnEntityComponentRemoveListener<E>> onEntityComponentRemove
			= new Listenable<>(((listener, objects) -> listener.onEntityComponentRemove((E) objects[0], (Component) objects[1])));

	private int NEXT_ENTITY_ID = 0;

	protected final Map<Class<? extends Component>, Map<E, Component>> components = new HashMap<>();
	protected final Map<Integer, E> entities = new HashMap<>();

	public E addEntity(E entity, Component... components)
	{
		int id = this.getNextAvailableEntityID();
		entity.setEntityID(id);
		entity.setEntityManager(this);
		this.entities.put(entity.getEntityID(), entity);

		for(Component component : components)
		{
			this.addComponentToEntity(entity, component);
		}

		entity.onEntityCreate(this);

		this.onEntityCreate.notifyListeners(entity);

		return entity;
	}

	public E removeEntity(E entity)
	{
		if (entity.getEntityManager() != this) throw new IllegalArgumentException("Cannot remove entity that the manager does not own!");
		if (entity.getEntityID() == -1) throw new IllegalArgumentException("Cannot remove entity not yet initialized!");

		entity.onEntityDestroy();

		this.onEntityDestroy.notifyListeners(entity);

		this.clearComponentFromEntity(entity);

		E result = this.entities.remove(entity.getEntityID());
		if (result == null) throw new IllegalArgumentException("Cannot find entity '" + entity.getClass().getSimpleName() + "' with id '" + entity.getEntityID() + "' to remove!");

		entity.setEntityManager(null);
		entity.setEntityID(-1);

		return result;
	}

	public void clear()
	{
		this.components.clear();
		this.entities.clear();
	}

	public boolean addComponentToEntity(E entity, Component component)
	{
		if (component == null) return false;

		Map<E, Component> entityComponentMap = this.components.computeIfAbsent(component.getClass(), key -> new HashMap<>());
		Component result = entityComponentMap.put(entity, component);

		if (result != null) throw new IllegalArgumentException("Cannot add component '" + component.getClass().getSimpleName() + "', since there exists another component of the same type in the same entity!");

		this.onEntityComponentAdd.notifyListeners(entity, component);

		return true;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponentFromEntity(E entity, Class<T> type)
	{
		Map<E, Component> entityComponentMap = this.components.get(type);
		if (entityComponentMap == null) return null;

		T result = (T) entityComponentMap.remove(entity);

		this.onEntityComponentRemove.notifyListeners(entity, result);

		return result;
	}

	public void clearComponentFromEntity(E entity)
	{
		for(Map<E, Component> entityComponentMap : this.components.values())
		{
			Component component = entityComponentMap.remove(entity);

			this.onEntityComponentRemove.notifyListeners(entity, component);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentFromEntity(E entity, Class<? extends T> type)
	{
		Map<E, Component> entityComponentMap = this.components.get(type);

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

	public E getEntityByComponent(Component component)
	{
		Map<E, Component> entityComponentMap = this.components.get(component.getClass());
		if (entityComponentMap == null) return null;
		for(Map.Entry<E, Component> entry : entityComponentMap.entrySet())
		{
			if (entry.getValue() == component) return entry.getKey();
		}
		return null;
	}

	public E getEntityByID(int id)
	{
		return this.entities.get(id);
	}

	public int getNextAvailableEntityID()
	{
		return NEXT_ENTITY_ID++;
	}

	public Iterable<E> getEntities()
	{
		return this.entities.values();
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getComponents(Class<? extends T> type, Collection<T> dst)
	{
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			if (type.isAssignableFrom(componentType))
			{
				Map<E, Component> entityComponentMap = this.components.get(componentType);
				dst.addAll((Collection<? extends T>) entityComponentMap.values());
			}
		}
		return dst;
	}

	public Collection<E> getEntities(Class<? extends Component>[] types, Collection<E> dst)
	{
		boolean flag = false;
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			for(Class<? extends Component> type : types)
			{
				if (type.isAssignableFrom(componentType))
				{
					Map<E, Component> entityComponentMap = this.components.get(componentType);
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

	public Set<E> getEntitiesWithAny(Class<? extends Component>[] types, Set<E> dst)
	{
		for(Class<? extends Component> componentType : this.components.keySet())
		{
			for(Class<? extends Component> type : types)
			{
				if (type.isAssignableFrom(componentType))
				{
					Map<E, Component> entityComponentMap = this.components.get(componentType);
					dst.addAll(entityComponentMap.keySet());
				}
			}
		}
		return dst;
	}
}
