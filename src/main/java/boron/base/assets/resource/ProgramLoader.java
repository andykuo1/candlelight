package boron.base.assets.resource;

import boron.base.asset.Asset;
import boron.base.asset.ResourceLoader;
import boron.base.asset.ResourceParameter;

import boron.bstone.mogli.Program;
import boron.bstone.mogli.Shader;

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
