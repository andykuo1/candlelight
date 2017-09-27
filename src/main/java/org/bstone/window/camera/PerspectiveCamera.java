package org.bstone.window.camera;

import org.bstone.transform.InvertedTransform3;
import org.joml.Matrix4f;
import org.qsilver.poma.Poma;

/**
 * Created by Andy on 4/6/17.
 */
public class PerspectiveCamera extends Camera
{
	private float fieldOfView = 70F;
	private float clippingNear = 0.01F;
	private float clippingFar = 100F;
	private float aspectRatio;

	public PerspectiveCamera(float x, float y, float z, float width, float height)
	{
		super(new InvertedTransform3().setPosition(x, y, z));
		this.aspectRatio = width / height;
	}

	public PerspectiveCamera(float width, float height)
	{
		this(0, 0, 0, width, height);
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

	public float getFarPlane()
	{
		return this.clippingFar;
	}

	public float getNearPlane()
	{
		return this.clippingNear;
	}

	public float getFieldOfView()
	{
		return this.fieldOfView;
	}

	@Override
	protected void updateProjectionMatrix(Matrix4f mat)
	{
		mat.setPerspective(this.fieldOfView, this.aspectRatio, this.clippingNear, this.clippingFar);
	}

	@Override
	protected void updateRotationMatrix(Matrix4f mat)
	{
		this.transform.quaternion().get(mat);
	}

	@Override
	protected void updateViewMatrix(Matrix4f mat)
	{
		mat.rotation(this.transform.quaternion()).translate(-this.transform.position.x, -this.transform.position.y, -this.transform.position.z);
	}
}
