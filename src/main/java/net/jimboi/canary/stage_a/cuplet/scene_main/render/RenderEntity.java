package net.jimboi.canary.stage_a.cuplet.scene_main.render;

import net.jimboi.canary.stage_a.cuplet.model.Model;
import net.jimboi.canary.stage_a.cuplet.scene_main.Renderable;

import org.joml.Matrix4f;

/**
 * Created by Andy on 11/2/17.
 */
public class RenderEntity implements Renderable
{
	@Override
	public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		return null;
	}

	@Override
	public Model getRenderModel()
	{
		return null;
	}
}
