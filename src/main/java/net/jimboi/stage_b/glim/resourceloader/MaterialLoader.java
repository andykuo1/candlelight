package net.jimboi.stage_b.glim.resourceloader;

import net.jimboi.stage_b.gnome.asset.ResourceLoader;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;

import org.bstone.material.Material;
import org.bstone.material.MaterialManager;
import org.bstone.material.Property;

/**
 * Created by Andy on 6/10/17.
 */
public class MaterialLoader implements ResourceLoader<Material, MaterialLoader.MaterialParameter>
{
	private final MaterialManager materialManager;

	public MaterialLoader(MaterialManager materialManager)
	{
		this.materialManager = materialManager;
	}

	@Override
	public Material load(MaterialParameter args)
	{
		return this.materialManager.createMaterial(args.properties);
	}

	public static class MaterialParameter implements ResourceParameter<Material>
	{
		public Property[] properties;

		public MaterialParameter(Property... properties)
		{
			this.properties = properties;
		}
	}
}
