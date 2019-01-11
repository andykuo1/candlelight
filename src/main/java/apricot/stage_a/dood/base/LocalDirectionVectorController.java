package apricot.stage_a.dood.base;

import apricot.bstone.transform.DirectionVector3;
import apricot.bstone.transform.Transform3;
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
