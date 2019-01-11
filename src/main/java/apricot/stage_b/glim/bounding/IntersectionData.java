package apricot.stage_b.glim.bounding;

import org.joml.Vector3fc;

/**
 * Created by Andy on 6/3/17.
 */
public class IntersectionData
{
	public final Bounding collider;
	public final Vector3fc pos;
	public final Vector3fc normal;
	public final Vector3fc delta;

	public IntersectionData(Bounding collider, Vector3fc pos, Vector3fc normal, Vector3fc delta)
	{
		this.collider = collider;
		this.pos = pos;
		this.normal = normal;
		this.delta = delta;
	}
}
