package boron.base.assets.resource;

import boron.base.asset.ResourceLoader;
import boron.base.asset.ResourceParameter;

import boron.bstone.mogli.Shader;
import boron.qsilver.ResourceLocation;

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
