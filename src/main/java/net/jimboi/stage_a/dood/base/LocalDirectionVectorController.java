package net.jimboi.stage_a.dood.base;

import org.bstone.transform.DirectionVector3;
import org.bstone.transform.Transform3;
import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public class LocalDirectionVectorController extends LocalDirectionVectorBase
{
	private final DirectionVector3 camera;

	public LocalDirectionVectorController(Transform3 transform, DirectionVector3 camera)
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
