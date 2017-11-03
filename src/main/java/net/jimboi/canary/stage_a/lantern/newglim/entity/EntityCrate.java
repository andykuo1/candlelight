package net.jimboi.canary.stage_a.lantern.newglim.entity;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.property.OldPropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.OldPropertyShadow;
import net.jimboi.apricot.base.renderer.property.OldPropertySpecular;
import net.jimboi.apricot.base.renderer.property.OldPropertyTexture;
import net.jimboi.canary.stage_a.lantern.newglim.WorldGlim;
import net.jimboi.canary.stage_a.lantern.newglim.bounding.square.AABB;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentBounding;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentRenderable;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentTransform;

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
								new OldPropertyDiffuse(),
								new OldPropertySpecular(),
								new OldPropertyShadow(true, true),
								new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "crate"))),
						"diffuse")),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
