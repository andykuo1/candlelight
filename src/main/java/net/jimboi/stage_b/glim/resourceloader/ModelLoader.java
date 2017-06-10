package net.jimboi.stage_b.glim.resourceloader;

import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.ResourceLoader;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;

import org.bstone.mogli.Mesh;
import org.qsilver.model.Model;

/**
 * Created by Andy on 6/10/17.
 */
public class ModelLoader implements ResourceLoader<Model, ModelLoader.ModelParameter>
{
	@Override
	public Model load(ModelParameter args)
	{
		return new Model(args.mesh.getSource());
	}

	public static class ModelParameter implements ResourceParameter<Model>
	{
		public Asset<Mesh> mesh;

		public ModelParameter(Asset<Mesh> mesh)
		{
			this.mesh = mesh;
		}
	}
}
