package apricot.base.assets.resource;

import apricot.base.asset.Asset;
import apricot.base.asset.ResourceLoader;
import apricot.base.asset.ResourceParameter;

import apricot.bstone.mogli.Program;
import apricot.bstone.mogli.Shader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andy on 6/10/17.
 */
public class ProgramLoader implements ResourceLoader<Program, ProgramLoader.ProgramParameter>
{
	@Override
	public Program load(ProgramParameter args)
	{
		Shader[] shaders = new Shader[args.shaders.size()];
		for (int i = 0; i < shaders.length; ++i)
		{
			shaders[i] = args.shaders.get(i).getSource();
		}
		return new Program().link(shaders);
	}

	public static class ProgramParameter implements ResourceParameter<Program>
	{
		public List<Asset<Shader>> shaders = new ArrayList<>();

		public ProgramParameter(Asset<Shader>... shaders)
		{
			Collections.addAll(this.shaders, shaders);
		}

		public ProgramParameter(Collection<Asset<Shader>> shaders)
		{
			this.shaders.addAll(shaders);
		}
	}
}
