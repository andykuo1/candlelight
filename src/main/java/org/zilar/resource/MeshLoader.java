package org.zilar.resource;

import net.jimboi.boron.base_ab.asset.ResourceLoader;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

import org.bstone.mogli.Mesh;
import org.qsilver.ResourceLocation;
import org.zilar.loader.OBJLoader;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;

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
