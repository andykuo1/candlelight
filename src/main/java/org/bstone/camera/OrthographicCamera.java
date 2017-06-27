package org.bstone.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.poma.Poma;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 5/22/17.
 */
public class OrthographicCamera extends Camera
{
	public static final Vector3fc NZAXIS = new Vector3f(0, 0, -1);

	private final Transform3Quat transform = new Transform3Quat()
	{
		@Override
		public Vector3f getForward(Vector3f dst)
		{
			this.rotation.conjugate();
			dst.set(0, 0, -1).rotate(this.rotation).normalize();
			this.rotation.conjugate();
			return dst;
		}
	};

	private float leftBound = -10F;
	private float rightBound = 10F;
	private float bottomBound = -10F;
	private float topBound = 10F;

	private float nearBound = -10F;
	private float farBound = 10F;

	private float aspectRatio = 1F;

	public OrthographicCamera(float width, float height)
	{
		this.aspectRatio = width / height;
	}

	@Override
	public Transform3Quat getTransform()
	{
		return this.transform;
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
		mat.ortho(this.leftBound, this.rightBound, this.bottomBound, this.topBound, this.nearBound, this.farBound);
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
