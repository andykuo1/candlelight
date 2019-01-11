package canary.lantern.scene_main.entity;

import canary.base.MaterialProperty;
import canary.base.Model;
import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.lantern.Lantern;
import canary.lantern.scene_main.component.ComponentBounding;
import canary.lantern.scene_main.component.ComponentRenderable;
import canary.lantern.scene_main.component.ComponentTransform;

import canary.bstone.asset.AssetManager;
import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 11/3/17.
 */
public class EntityCrate extends EntityBase
{
	@Override
	protected void onEntitySetup()
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
	protected void onUpdate()
	{
		super.onUpdate();

		ComponentTransform componentTransform = this.getComponent(ComponentTransform.class);
		componentTransform.transform.rotation.rotateXYZ(0, 0.001F, 0);
	}
}
