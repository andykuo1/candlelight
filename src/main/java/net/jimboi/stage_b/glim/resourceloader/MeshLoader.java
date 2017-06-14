package net.jimboi.stage_b.glim.resourceloader;

import net.jimboi.stage_b.gnome.asset.ResourceLoader;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;
import net.jimboi.stage_b.gnome.meshbuilder.MeshData;
import net.jimboi.stage_b.gnome.meshbuilder.ModelUtil;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.mogli.Mesh;
import org.qsilver.loader.OBJLoader;

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
