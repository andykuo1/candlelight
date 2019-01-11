package apricot.stage_b.glim.entity;

import apricot.base.OldGameEngine;
import apricot.base.animation.AnimatorSpriteSheet;
import apricot.base.assets.resource.TextureAtlasLoader;
import apricot.base.entity.Entity;
import apricot.base.render.OldModel;
import apricot.base.renderer.BillboardRenderer;
import apricot.base.renderer.property.OldPropertyDiffuse;
import apricot.base.renderer.property.OldPropertyShadow;
import apricot.base.renderer.property.OldPropertyTexture;
import apricot.stage_b.glim.WorldGlim;
import apricot.stage_b.glim.bounding.square.AABB;
import apricot.stage_b.glim.entity.component.EntityComponentBillboard;
import apricot.stage_b.glim.entity.component.EntityComponentBounding;
import apricot.stage_b.glim.entity.component.EntityComponentHeading;
import apricot.stage_b.glim.entity.component.EntityComponentNavigator;
import apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import apricot.stage_b.glim.entity.component.EntityComponentTargeter;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;
import apricot.base.asset.Asset;
import apricot.base.sprite.SpriteSheet;
import apricot.base.sprite.TextureAtlas;
import apricot.base.sprite.TextureAtlasBuilder;
import apricot.base.sprite.TextureAtlasData;

import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Texture;
import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityBunny extends EntityGlim
{
	public static Entity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);

		Asset<Texture> bunny = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny");

		if (!OldGameEngine.ASSETMANAGER.containsAsset(TextureAtlas.class, "bunny"))
		{
			TextureAtlasBuilder tab = new TextureAtlasBuilder(bunny, 144, 48);
			tab.addHorizontalStrip(0, 0, 48, 48, 0, 3);
			TextureAtlasData data = tab.bake();
			tab.clear();

			OldGameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "bunny", new TextureAtlasLoader.TextureAtlasParameter(data));
		}

		Asset<TextureAtlas> atlas = OldGameEngine.ASSETMANAGER.getAsset(TextureAtlas.class, "bunny");
		SpriteSheet bunnySheet = new SpriteSheet(atlas, new int[] {0, 1, 2});
		SCENE.getAnimationManager().start(new AnimatorSpriteSheet(bunnySheet, 0.2F));

		return MANAGER.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, new OldModel(
						OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
						SCENE.getMaterialManager().createMaterial(
								new OldPropertyDiffuse(),
								new OldPropertyShadow(true, true),
								new OldPropertyTexture(bunnySheet).setTransparent(true)),
						"diffuse")),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.2F, 0.2F))),
				new EntityComponentHeading( 0.2F, 1F),
				new EntityComponentBillboard(BillboardRenderer.Type.CYLINDRICAL),
				new EntityComponentTargeter(0.1F),
				new EntityComponentNavigator(world, world.createNavigator()));
	}
}
