package net.jimboi.stage_a.dood.base;

import net.jimboi.stage_b.gnome.transform.DirectionVectors;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public class LocalDirectionVectorController extends LocalDirectionVectorBase
{
	private final DirectionVectors camera;

	public LocalDirectionVectorController(Transform3 transform, DirectionVectors camera)
	{
		super(transform);
		this.camera = camera;
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.camera.getRight(dst);
	}
}
