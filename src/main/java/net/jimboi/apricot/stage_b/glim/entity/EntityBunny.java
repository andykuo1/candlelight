package net.jimboi.apricot.stage_b.glim.entity;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.animation.AnimatorSpriteSheet;
import net.jimboi.apricot.base.assets.resource.TextureAtlasLoader;
import net.jimboi.apricot.base.entity.Entity;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.BillboardRenderer;
import net.jimboi.apricot.base.renderer.property.OldPropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.OldPropertyShadow;
import net.jimboi.apricot.base.renderer.property.OldPropertyTexture;
import net.jimboi.apricot.stage_b.glim.WorldGlim;
import net.jimboi.apricot.stage_b.glim.bounding.square.AABB;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBillboard;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentHeading;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentNavigator;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTargeter;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.sprite.SpriteSheet;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;
import net.jimboi.boron.base_ab.sprite.TextureAtlasBuilder;
import net.jimboi.boron.base_ab.sprite.TextureAtlasData;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;

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
