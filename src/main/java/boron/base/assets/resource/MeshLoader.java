package boron.base.assets.resource;

import boron.base.asset.ResourceLoader;
import boron.base.asset.ResourceParameter;

import boron.bstone.mogli.Mesh;
import boron.qsilver.ResourceLocation;
import boron.zilar.loader.OBJLoader;
import boron.zilar.meshbuilder.MeshData;
import boron.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 6/10/17.
 */
public class MeshLoader implements ResourceLoader<Mesh, MeshLoader.MeshParameter>
{
	@Override
	public Mesh load(MeshParameter args)
	{
		if (args.data != null)
		{
			return args.staticMesh ? ModelUtil.createStaticMesh(args.data) : ModelUtil.createDynamicMesh(args.data);
		}

		if (args.location != null)
		{
			return OBJLoader.read(args.location);
		}

		return new Mesh();
	}

	public static class MeshParameter implements ResourceParameter<Mesh>
	{
		public ResourceLocation location;

		public MeshData data;
		public boolean staticMesh = true;

		public MeshParameter(ResourceLocation location)
		{
			this.location = location;
		}

		public MeshParameter(MeshData data, boolean staticMesh)
		{
			this.data = data;
			this.staticMesh = staticMesh;
		}

		public MeshParameter(MeshData data)
		{
			this(data, true);
		}
	}
}
