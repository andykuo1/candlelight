package net.jimboi.boron.stage_a.shroom.component;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3c;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.zilar.model.Model;

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

	private static final Vector3f VEC = new Vector3f();

	@Override
	public Matrix4f getRenderTransformation(Matrix4f dst)
	{
		super.getRenderTransformation(dst);
		this.camera.position3().sub(this.transform.getPosition(VEC), VEC);
		dst.rotateXYZ(0, (float) -Math.atan2(VEC.z(), VEC.x()) + Transform.HALF_PI, 0);
		return dst;
	}
}
