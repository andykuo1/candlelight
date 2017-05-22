package org.bstone.camera;

import net.jimboi.mod.transform.Transform3Q;

import org.bstone.poma.Poma;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/22/17.
 */
public class OrthographicCamera implements Camera
{
	public static final Vector3fc NZAXIS = new Vector3f(0, 0, -1);

	private final Transform3Q transform = new Transform3Q()
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

	private final Vector3f target = new Vector3f(0, 0, 0);

	private Matrix4fc viewMatrix;
	private Matrix4fc rotationMatrix;
	private Matrix4fc projectionMatrix;

	public OrthographicCamera(float width, float height)
	{
		this.aspectRatio = width / height;

		this.updateProjectionMatrix();
		this.updateRotationMatrix();
		this.updateViewMatrix();
	}

	@Override
	public Transform3Q getTransform()
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
	public Matrix4fc orientation()
	{
		this.updateRotationMatrix();
		return this.rotationMatrix;
	}

	@Override
	public Matrix4fc projection()
	{
		this.updateProjectionMatrix();
		return this.projectionMatrix;
	}

	@Override
	public Matrix4fc view()
	{
		this.updateViewMatrix();
		return this.viewMatrix;
	}

	private void updateProjectionMatrix()
	{
		this.projectionMatrix = new Matrix4f().ortho(this.leftBound, this.rightBound, this.bottomBound, this.topBound, this.nearBound, this.farBound);
	}

	private void updateRotationMatrix()
	{
		this.rotationMatrix = this.transform.rotation().get(new Matrix4f());
	}

	private void updateViewMatrix()
	{
		this.viewMatrix = new Matrix4f().rotate(this.transform.rotation()).translate(-this.transform.position.x, -this.transform.position.y, -this.transform.position.z);
	}
}
