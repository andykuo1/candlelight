package net.jimboi.apricot.stage_b.glim.entity;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.property.PropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.PropertyShadow;
import net.jimboi.apricot.base.renderer.property.PropertySpecular;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.apricot.stage_b.glim.WorldGlim;
import net.jimboi.apricot.stage_b.glim.bounding.square.AABB;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.zilar.entity.Entity;

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
								new PropertyDiffuse(),
								new PropertySpecular(),
								new PropertyShadow(true, true),
								new PropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "crate"))),
						"diffuse")),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
