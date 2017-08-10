package net.jimboi.apricot.stage_b.glim.entity;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.BillboardRenderer;
import net.jimboi.apricot.base.renderer.property.PropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.PropertyShadow;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.apricot.stage_b.glim.WorldGlim;
import net.jimboi.apricot.stage_b.glim.bounding.square.AABB;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBillboard;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentHeading;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentNavigator;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTargeter;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.qsilver.asset.Asset;
import org.qsilver.resource.TextureAtlasLoader;
import org.zilar.animation.AnimatorSpriteSheet;
import org.zilar.entity.Entity;
import org.zilar.sprite.SpriteSheet;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;
import org.zilar.sprite.TextureAtlasData;

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
								new PropertyDiffuse(),
								new PropertyShadow(true, true),
								new PropertyTexture(bunnySheet).setTransparent(true)),
						"diffuse")),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.2F, 0.2F))),
				new EntityComponentHeading( 0.2F, 1F),
				new EntityComponentBillboard(BillboardRenderer.Type.CYLINDRICAL),
				new EntityComponentTargeter(0.1F),
				new EntityComponentNavigator(world, world.createNavigator()));
	}
}
