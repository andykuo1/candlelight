package net.jimboi.dood.system;

import net.jimboi.dood.component.ComponentSpriteSheet;
import net.jimboi.mod.RenderUtil;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.render.Material;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemAnimatedTexture extends EntitySystem implements Scene.OnSceneRenderListener
{
	protected final Scene scene;

	public SystemAnimatedTexture(EntityManager entityManager, Scene scene)
	{
		super(entityManager);
		this.scene = scene;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(this.scene.onSceneRender);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	private int counter;

	@Override
	public void onSceneRender()
	{
		this.counter++;
		if (counter > 10)
		{
			this.counter = 0;
			Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentSpriteSheet.class);
			for (Entity entity : entities)
			{
				ComponentSpriteSheet componentSpriteSheet = entity.getComponent(ComponentSpriteSheet.class);
				Material material = RenderUtil.getMaterial(componentSpriteSheet.materialID);
				material.sprite = componentSpriteSheet.spriteSheet.get();
			}
		}
	}
}
