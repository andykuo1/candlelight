package net.jimboi.glim.resourceloader;

import net.jimboi.mod2.asset.ResourceLoader;
import net.jimboi.mod2.asset.ResourceParameter;

import org.qsilver.material.Material;
import org.qsilver.material.MaterialManager;
import org.qsilver.material.Property;

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
