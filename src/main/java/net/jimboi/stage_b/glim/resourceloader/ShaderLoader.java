package net.jimboi.stage_b.glim.resourceloader;

import net.jimboi.stage_b.gnome.asset.ResourceLoader;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.mogli.Shader;

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
