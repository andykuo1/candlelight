package apricot.base.assets.resource;

import apricot.base.asset.ResourceLoader;
import apricot.base.asset.ResourceParameter;

import apricot.bstone.mogli.Shader;
import apricot.qsilver.ResourceLocation;

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
