package net.jimboi.stage_b.glim.system;

import net.jimboi.stage_b.glim.gameentity.GameEntity;
import net.jimboi.stage_b.glim.gameentity.GameEntityManager;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentSprite;

import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 6/13/17.
 */
public class EntitySystemSprite extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	public EntitySystemSprite(GameEntityManager entityManager, Scene scene)
	{
		super(entityManager);
		this.registerListenable(scene.onSceneUpdate);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	float time = 0;
	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<GameEntity> entities = this.entityManager.getEntitiesWithComponent(GameComponentSprite.class, GameComponentInstance.class);
		time += delta;
		if (time >= 10)
		{
			time = 0;

			for (GameEntity entity : entities)
			{
				GameComponentSprite componentSprite = entity.getComponent(GameComponentSprite.class);
				GameComponentInstance componentInstance = entity.getComponent(GameComponentInstance.class);
				componentSprite.sprite = componentSprite.spritesheet.get();
			}
		}
	}
}
