package org.bstone.camera;

import net.jimboi.mod.transform.Transform3Q;

import org.bstone.poma.Poma;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 4/6/17.
 */
public class PerspectiveCamera extends Camera
{
	public static final Vector3fc NZAXIS = new Vector3f(0, 0, -1);

	private final Transform3Q transform = new Transform3Q(){
		@Override
		public Vector3f getForward(Vector3f dst)
		{
			this.rotation.conjugate();
			dst.set(NZAXIS).rotate(this.rotation).normalize();
			this.rotation.conjugate();
			return dst;
		}
	};

	private float fieldOfView = 70F;
	private float clippingNear = 0.01F;
	private float clippingFar = 100F;
	private float aspectRatio;

	public PerspectiveCamera(float width, float height)
	{
		super();
		this.aspectRatio = width / height;
	}

	@Override
	public Transform3Q getTransform()
	{
		return this.transform;
	}

	public void setFieldOfView(float fov)
	{
		Poma.ASSERT(fov > 0 && fov < 180);

		this.fieldOfView = fov;
	}

	public void setClippingPlane(float nearPlane, float farPlane)
	{
		Poma.ASSERT(nearPlane > 0);
		Poma.ASSERT(farPlane > nearPlane);

		this.clippingNear = nearPlane;
		this.clippingFar = farPlane;
	}

	@Override
	protected void updateProjectionMatrix(Matrix4f mat)
	{
		mat.perspective(this.fieldOfView, this.aspectRatio, this.clippingNear, this.clippingFar);
	}

	@Override
	protected void updateRotationMatrix(Matrix4f mat)
	{
		this.transform.rotation().get(mat);
	}

	@Override
	protected void updateViewMatrix(Matrix4f mat)
	{
		mat.rotate(this.transform.rotation()).translate(-this.transform.position.x, -this.transform.position.y, -this.transform.position.z);
	}
}
