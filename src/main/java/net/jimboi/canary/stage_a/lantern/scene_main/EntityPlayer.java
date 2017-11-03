package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.canary.stage_a.cuplet.model.Model;
import net.jimboi.canary.stage_a.lantern.Lantern;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentRenderable;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentTransform;

import org.bstone.entity.EntityManager;
import org.bstone.material.Material;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 11/3/17.
 */
public class EntityPlayer extends EntityBase
{
	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		Model model = new Model(Lantern.getLantern().getFramework().getAssetManager().getAsset("mesh", "player"), new Material());
		Transform3 transform = new Transform3();
		this.addComponent(new ComponentTransform(transform));
		this.addComponent(new ComponentRenderable(transform, model));
	}
}
