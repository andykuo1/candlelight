package net.jimboi.stage_a.dood.system;

import net.jimboi.stage_a.dood.Resources;
import net.jimboi.stage_a.dood.component.ComponentSpriteSheet;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;
import net.jimboi.stage_a.mod.ModMaterial;

import org.qsilver.renderer.RenderManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemAnimatedTexture extends EntitySystem implements RenderManager.OnRenderUpdateListener
{
	protected final Scene scene;
	protected final RenderManager renderer;

	public SystemAnimatedTexture(EntityManager entityManager, Scene scene, RenderManager renderer)
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
				ModMaterial material = Resources.getMaterial(componentSpriteSheet.materialID);
				if (material.hasTexture())
				{
					material.sprite = componentSpriteSheet.spriteSheet.get();
				}
			}
		}
	}
}
