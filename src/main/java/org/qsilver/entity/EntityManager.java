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

	public final Listenable<EntityManager.OnEntityAddListener> onEntityAdd = new Listenable<>(((listener, objects) -> listener.onEntityAdd((Entity) objects[0])));
	public final Listenable<EntityManager.OnEntityRemoveListener> onEntityRemove = new Listenable<>((listener, objects) -> listener.onEntityRemove((Entity) objects[0]));

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
