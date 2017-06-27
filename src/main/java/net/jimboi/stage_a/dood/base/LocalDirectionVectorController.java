package net.jimboi.stage_a.dood.base;

import org.joml.Vector3f;
import org.qsilver.transform.DirectionVectors;
import org.zilar.transform.Transform3;

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
