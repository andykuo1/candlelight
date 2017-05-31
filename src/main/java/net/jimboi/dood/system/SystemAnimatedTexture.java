package net.jimboi.dood.system;

import net.jimboi.dood.Resources;
import net.jimboi.dood.component.ComponentSpriteSheet;
import net.jimboi.mod.renderer.Renderer;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.render.Material;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemAnimatedTexture extends EntitySystem implements Renderer.OnRenderUpdateListener
{
	protected final Scene scene;
	protected final Renderer renderer;

	public SystemAnimatedTexture(EntityManager entityManager, Scene scene, Renderer renderer)
	{
		super(entityManager);
		this.scene = scene;
		this.renderer = renderer;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(this.renderer.onRenderUpdate);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	private int counter;

	@Override
	public void onRenderUpdate()
	{
		this.counter++;
		if (counter > 10)
		{
			this.counter = 0;
			Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentSpriteSheet.class);
			for (Entity entity : entities)
			{
				ComponentSpriteSheet componentSpriteSheet = entity.getComponent(ComponentSpriteSheet.class);
				Material material = Resources.getMaterial(componentSpriteSheet.materialID);
				material.sprite = componentSpriteSheet.spriteSheet.get();
			}
		}
	}
}
