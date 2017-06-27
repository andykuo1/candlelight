package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBillboard;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentHeading;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentNavigator;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentSprite;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTargeter;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.glim.renderer.BillboardRenderer;
import net.jimboi.stage_b.gnome.material.property.PropertyDiffuse;
import net.jimboi.stage_b.gnome.material.property.PropertyShadow;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.sprite.SpriteSheet;
import net.jimboi.stage_b.gnome.sprite.TiledTextureAtlas;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityBunny extends EntityGlim
{
	public static Entity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = Transform3.create();
		transform.position.set(x, y, z);
		TiledTextureAtlas textureAtlas = new TiledTextureAtlas(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "bunny"), 48, 48);
		SpriteSheet spriteSheet = textureAtlas.createSpriteSheet(0, 1, 2);
		return MANAGER.createEntity(
				new GameComponentTransform(transform),
				new GameComponentInstance(transform, new Model(
						RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "plane"),
						RendererGlim.INSTANCE.getMaterialManager().createMaterial(
								new PropertyDiffuse(),
								new PropertyShadow(true, true),
								new PropertyTexture(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "bunny")).setTransparent(true)),
						"diffuse")),
				new GameComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.2F, 0.2F))),
				new GameComponentSprite(spriteSheet, 0.2F),
				new GameComponentHeading( 0.2F, 1F),
				new GameComponentBillboard(BillboardRenderer.Type.CYLINDRICAL),
				new GameComponentTargeter(0.1F),
				new GameComponentNavigator(world, world.createNavigator()));
	}
}
