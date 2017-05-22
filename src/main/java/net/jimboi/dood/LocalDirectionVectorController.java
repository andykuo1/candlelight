package net.jimboi.dood;

import net.jimboi.mod.transform.LocalDirectionVector;
import net.jimboi.mod.transform.Transform3;

import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public class LocalDirectionVectorController extends LocalDirectionVectorBase
{
	private final LocalDirectionVector camera;

	public LocalDirectionVectorController(Transform3 transform, LocalDirectionVector camera)
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
