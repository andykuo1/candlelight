package net.jimboi.boron.stage_a.base.livingentity;

import org.bstone.render.model.Model;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3c;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Andy on 7/19/17.
 */
public class EntityComponentRenderableBillboard extends EntityComponentRenderable
{
	private final Transform3c camera;

	public EntityComponentRenderableBillboard(Transform3c camera, Transform transform, Model model)
	{
		super(transform, model);

		this.camera = camera;
	}

	private static final Vector3f _VEC_A = new Vector3f();
	private static final Vector3f _VEC_B = new Vector3f();

	@Override
	public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		this.transform.getScale(_VEC_A);
		this.transform.getTransformation(dst).scale(1 / _VEC_A.x(), 1 / _VEC_A.y(), 1 / _VEC_A.z());
		this.camera.position3().sub(this.transform.getPosition(_VEC_B), _VEC_B);
		dst.rotateXYZ(0, (float) -Math.atan2(_VEC_B.z(), _VEC_B.x()) + Transform.HALF_PI, 0);
		dst.scale(_VEC_A);
		return dst;
	}
}
