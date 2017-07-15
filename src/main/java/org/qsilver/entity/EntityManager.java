package org.qsilver.entity;

import org.bstone.component.ComponentManager;
import org.bstone.util.listener.Listenable;

import java.util.Iterator;

/**
 * Created by Andy on 6/25/17.
 */
public class EntityManager extends ComponentManager<Entity, EntityComponent>
{
	public interface OnEntityAddListener
	{
		void onEntityAdd(Entity entity);
	}

	public interface OnEntityRemoveListener
	{
		void onEntityRemove(Entity entity);
	}

	public interface OnEntityComponentAddListener
	{
		void onEntityComponentAdd(Entity entity, EntityComponent component);
	}

	public interface OnEntityComponentRemoveListener
	{
		void onEntityComponentRemove(Entity entity, EntityComponent component);
	}

	public final Listenable<OnEntityAddListener> onEntityAdd = new Listenable<>(((listener, objects) -> listener.onEntityAdd((Entity) objects[0])));
	public final Listenable<OnEntityRemoveListener> onEntityRemove = new Listenable<>((listener, objects) -> listener.onEntityRemove((Entity) objects[0]));
	public final Listenable<OnEntityComponentAddListener> onEntityComponentAdd = new Listenable<>((listener, objects) -> listener.onEntityComponentAdd((Entity) objects[0], (EntityComponent) objects[1]));
	public final Listenable<OnEntityComponentRemoveListener> onEntityComponentRemove = new Listenable<>((listener, objects) -> listener.onEntityComponentRemove((Entity) objects[0], (EntityComponent) objects[1]));

	public EntityManager()
	{
		super(EntityComponent.class);
	}

	public void update()
	{
		Iterator<Entity> iter = this.entities.values().iterator();
		while(iter.hasNext())
		{
			Entity entity = iter.next();
			if (entity.isDead())
			{
				iter.remove();
				this.removeEntity(entity);
			}
		}
	}

	@Override
	protected void onComponentAdd(Entity entity, EntityComponent component)
	{
		super.onComponentAdd(entity, component);
		this.onEntityComponentAdd.notifyListeners(entity, component);
	}

	@Override
	protected void onComponentRemove(Entity entity, EntityComponent component)
	{
		super.onComponentRemove(entity, component);
		this.onEntityComponentRemove.notifyListeners(entity, component);
	}

	public Entity createEntity(EntityComponent... components)
	{
		return this.addEntity(new Entity(), components);
	}

	@Override
	protected <T extends Entity> T addEntity(T entity, EntityComponent... components)
	{
		T ret = super.addEntity(entity, components);
		this.onEntityAdd.notifyListeners(entity);
		return ret;
	}

	@Override
	protected <T extends Entity> T removeEntity(T entity)
	{
		T ret = super.removeEntity(entity);
		this.onEntityRemove.notifyListeners(entity);
		return ret;
	}
}
