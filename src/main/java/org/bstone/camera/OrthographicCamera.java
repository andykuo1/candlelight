package org.bstone.camera;

import org.bstone.transform.Transform3;
import org.joml.Matrix4f;
import org.qsilver.poma.Poma;

/**
 * A camera with an orthographic projection.
 *
 * Created by Andy on 5/22/17.
 */
public class OrthographicCamera extends Camera
{
	private float leftBound = -10F;
	private float rightBound = 10F;
	private float bottomBound = -10F;
	private float topBound = 10F;

	private float nearBound = -100F;
	private float farBound = 100F;

	private float aspectRatio;

	public OrthographicCamera(float x, float y, float z, float width, float height)
	{
		super(new Transform3().setPosition(x, y, z));
		this.aspectRatio = width / height;
	}

	public OrthographicCamera(float width, float height)
	{
		this(0, 0, 0, width, height);
	}

	public void setClippingBound(float left, float right, float top, float bottom)
	{
		this.leftBound = left;
		this.rightBound = right;
		this.topBound = top;
		this.bottomBound = bottom;
	}

	public void setClippingPlane(float nearPlane, float farPlane)
	{
		Poma.ASSERT(nearPlane > 0);
		Poma.ASSERT(farPlane > nearPlane);

		this.nearBound = nearPlane;
		this.farBound = farPlane;
	}

	@Override
	protected void updateProjectionMatrix(Matrix4f mat)
	{
		mat.setOrtho(this.leftBound, this.rightBound, this.bottomBound, this.topBound, this.nearBound, this.farBound);
	}

	@Override
	protected void updateRotationMatrix(Matrix4f mat)
	{
		this.transform.quaternion().get(mat);
	}

	@Override
	protected void updateViewMatrix(Matrix4f mat)
	{
		mat.rotate(this.transform.quaternion()).translate(-this.transform.position3().x(), -this.transform.position3().y(), -this.transform.position3().z());
	}
}
