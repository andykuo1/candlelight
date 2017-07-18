package net.jimboi.apricot.stage_a.dood.system;

import net.jimboi.apricot.stage_a.dood.Resources;
import net.jimboi.apricot.stage_a.dood.component.ComponentSpriteSheet;
import net.jimboi.apricot.stage_a.dood.entity.Entity;
import net.jimboi.apricot.stage_a.dood.entity.EntityManager;
import net.jimboi.apricot.stage_a.mod.ModMaterial;
import net.jimboi.apricot.stage_a.mod.renderer.OldRenderService;

import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemAnimatedTexture extends EntitySystem implements OldRenderService.OnRenderUpdateListener
{
	protected final Scene scene;
	protected final OldRenderService renderer;

	public SystemAnimatedTexture(EntityManager entityManager, Scene scene, OldRenderService renderer)
	{
		super(entityManager);
		this.scene = scene;
		this.renderer = renderer;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(this.renderer.onRender);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	private int counter;

	@Override
	public void onRender()
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
