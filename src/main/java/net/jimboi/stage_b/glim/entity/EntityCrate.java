package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.qsilver.entity.Entity;
import org.zilar.material.property.PropertyDiffuse;
import org.zilar.material.property.PropertyShadow;
import org.zilar.material.property.PropertySpecular;
import org.zilar.material.property.PropertyTexture;
import org.zilar.model.Model;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityCrate extends EntityGlim
{
	public static Entity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = Transform3.create();
		transform.position.set(x, y, z);
		return MANAGER.createEntity(
				new GameComponentTransform(transform),
				new GameComponentInstance(transform, new Model(
						RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "box"),
						RendererGlim.INSTANCE.getMaterialManager().createMaterial(
								new PropertyDiffuse(),
								new PropertySpecular(),
								new PropertyShadow(true, true),
								new PropertyTexture(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "crate"))),
						"diffuse")),
				new GameComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
