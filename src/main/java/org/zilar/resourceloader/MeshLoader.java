package org.zilar.resourceloader;

import org.bstone.mogli.Mesh;
import org.qsilver.asset.ResourceLoader;
import org.qsilver.asset.ResourceParameter;
import org.qsilver.loader.OBJLoader;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.resource.ResourceLocation;

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
			return ModelUtil.createMesh(args.data);
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

		public MeshParameter(ResourceLocation location)
		{
			this.location = location;
		}

		public MeshParameter(MeshData data)
		{
			this.data = data;
		}
	}
}
