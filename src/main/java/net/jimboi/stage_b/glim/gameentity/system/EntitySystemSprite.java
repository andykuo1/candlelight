package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentSprite;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;
import org.zilar.material.property.PropertyTexture;

import java.util.Collection;

/**
 * Created by Andy on 6/13/17.
 */
public class EntitySystemSprite extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	public EntitySystemSprite(EntityManager entityManager, Scene scene)
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

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(GameComponentSprite.class, GameComponentInstance.class);
		for (Entity entity : entities)
		{
			GameComponentSprite componentSprite = entity.getComponent(GameComponentSprite.class);
			componentSprite.framedelta += delta;
			if (componentSprite.framedelta >= componentSprite.frametime)
			{
				componentSprite.framedelta = 0;
				GameComponentInstance componentInstance = entity.getComponent(GameComponentInstance.class);
				PropertyTexture propertyTexture = componentInstance.getModel().getMaterial().getComponent(PropertyTexture.class);
				propertyTexture.sprite = componentSprite.spritesheet.get();
			}
		}
	}
}
