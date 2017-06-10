package net.jimboi.glim.resourceloader;

import net.jimboi.mod2.asset.ResourceLoader;
import net.jimboi.mod2.asset.ResourceParameter;
import net.jimboi.mod2.resource.ResourceLocation;

import org.bstone.loader.OBJLoader;
import org.bstone.mogli.Mesh;

/**
 * Created by Andy on 6/10/17.
 */
public class MeshLoader implements ResourceLoader<Mesh, MeshLoader.MeshParameter>
{
	@Override
	public Mesh load(MeshParameter args)
	{
		if (args.mesh != null)
		{
			return args.mesh;
		}

		if (args.location != null)
		{
			return OBJLoader.read(args.location.getFilePath());
		}

		return new Mesh();
	}

	public static class MeshParameter implements ResourceParameter<Mesh>
	{
		public ResourceLocation location;
		public Mesh mesh;

		public MeshParameter(ResourceLocation location)
		{
			this.location = location;
		}

		public MeshParameter(Mesh mesh)
		{
			this.mesh = mesh;
		}
	}
}
