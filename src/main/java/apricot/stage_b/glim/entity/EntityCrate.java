package apricot.stage_b.glim.entity;

import apricot.base.OldGameEngine;
import apricot.base.entity.Entity;
import apricot.base.render.OldModel;
import apricot.base.renderer.property.OldPropertyDiffuse;
import apricot.base.renderer.property.OldPropertyShadow;
import apricot.base.renderer.property.OldPropertySpecular;
import apricot.base.renderer.property.OldPropertyTexture;
import apricot.stage_b.glim.WorldGlim;
import apricot.stage_b.glim.bounding.square.AABB;
import apricot.stage_b.glim.entity.component.EntityComponentBounding;
import apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;

import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Texture;
import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityCrate extends EntityGlim
{
	public static Entity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		return MANAGER.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, new OldModel(
						OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "box"),
						SCENE.getMaterialManager().createMaterial(
								new OldPropertyDiffuse(),
								new OldPropertySpecular(),
								new OldPropertyShadow(true, true),
								new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "crate"))),
						"diffuse")),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
