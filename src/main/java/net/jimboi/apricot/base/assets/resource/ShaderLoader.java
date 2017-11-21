package net.jimboi.apricot.base.assets.resource;

import net.jimboi.boron.base_ab.asset.ResourceLoader;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

import org.bstone.mogli.Shader;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 6/10/17.
 */
public class ShaderLoader implements ResourceLoader<Shader, ShaderLoader.ShaderParameter>
{
	@Override
	public Shader load(ShaderParameter args)
	{
		return new Shader(args.location, args.type);
	}

	public static class ShaderParameter implements ResourceParameter<Shader>
	{
		public ResourceLocation location;
		public int type;

		public ShaderParameter(ResourceLocation location, int type)
		{
			this.location = location;
			this.type = type;
		}
	}
}