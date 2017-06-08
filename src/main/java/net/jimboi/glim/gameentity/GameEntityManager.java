package net.jimboi.glim.gameentity;

import net.jimboi.glim.component.GameComponent;

import org.bstone.component.ComponentManager;
import org.bstone.util.listener.Listenable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 6/8/17.
 */
public class GameEntityManager extends ComponentManager<GameEntity, GameComponent>
{
	public interface OnGameEntityAddListener
	{
		void onGameEntityAdd(GameEntity entity);
	}

	public interface OnGameEntityRemoveListener
	{
		void onGameEntityRemove(GameEntity entity);
	}

	public final Listenable<OnGameEntityAddListener> onGameEntityAdd = new Listenable<>(((listener, objects) -> listener.onGameEntityAdd((GameEntity) objects[0])));
	public final Listenable<OnGameEntityRemoveListener> onGameEntityRemove = new Listenable<>((listener, objects) -> listener.onGameEntityRemove((GameEntity) objects[0]));

	private Set<GameEntity> destroyCache = new HashSet<>();

	public GameEntityManager()
	{
		super(GameComponent.class);
	}

	public void update()
	{
		for (GameEntity entity : this.entities.values())
		{
			if (entity.isDead())
			{
				this.destroyCache.add(entity);
			}
		}

		for (GameEntity entity : this.destroyCache)
		{
			this.removeEntity(entity);
		}
		this.destroyCache.clear();
	}

	public GameEntity createEntity(GameComponent... components)
	{
		return this.addEntity(new GameEntity(), components);
	}

	@Override
	protected <T extends GameEntity> T addEntity(T entity, GameComponent... components)
	{
		T ret = super.addEntity(entity, components);
		this.onGameEntityAdd.notifyListeners(entity);
		return ret;
	}

	@Override
	protected <T extends GameEntity> T removeEntity(T entity)
	{
		T ret = super.removeEntity(entity);
		this.onGameEntityRemove.notifyListeners(entity);
		return ret;
	}
}
