package net.jimboi.canary.stage_a.lantern.scene_main.entity;

import net.jimboi.canary.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.canary.stage_a.base.model.Model;
import net.jimboi.canary.stage_a.base.renderer.MaterialProperty;
import net.jimboi.canary.stage_a.lantern.Lantern;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentBounding;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentRenderable;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentTransform;

import org.bstone.asset.AssetManager;
import org.bstone.entity.EntityManager;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 11/3/17.
 */
public class EntityCrate extends EntityBase
{
	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		final AssetManager assets = Lantern.getLantern().getAssetManager();

		Model model = new Model(assets.getAsset("mesh", "cube"));
		model.material().setProperty(MaterialProperty.TEXTURE, assets.getAsset("texture", "crate"));

		Transform3 transform = new Transform3();
		this.addComponent(new ComponentTransform(transform));
		this.addComponent(new ComponentRenderable(transform, model));
		this.addComponent(new ComponentBounding(new AxisAlignedBoundingBox(0, 0, 0.5F, 0.5F)));
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		ComponentTransform componentTransform = this.getComponent(ComponentTransform.class);
		componentTransform.transform.rotation.rotate(0, 0.001F, 0);
	}
}
