package canary.bstone.transform;

import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/13/17.
 */
public class InvertedTransform3 extends Transform3
{
	public static final Vector3fc NZAXIS = new Vector3f(0, 0, -1);

	@Override
	public Vector3f getForward(Vector3f dst)
	{
		this.rotation.conjugate();
		dst.set(NZAXIS).rotate(this.rotation).normalize();
		this.rotation.conjugate();
		return dst;
	}
}
