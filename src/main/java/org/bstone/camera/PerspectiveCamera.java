package org.bstone.camera;

import net.jimboi.mod.transform.Transform3Q;

import org.bstone.util.poma.Poma;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 4/6/17.
 */
public class PerspectiveCamera implements Camera
{
	public static final Vector3fc NZAXIS = new Vector3f(0, 0, -1);

	private final Transform3Q transform = new Transform3Q(){
		@Override
		public Vector3f getForward(Vector3f dst)
		{
			this.rotation.conjugate();
			dst.set(0, 0, -1).rotate(this.rotation).normalize();
			this.rotation.conjugate();
			return dst;
		}
	};

	private float fieldOfView = 70F;
	private float clippingNear = 0.01F;
	private float clippingFar = 100F;
	private float aspectRatio;

	private final Vector3f target = new Vector3f(0, 0, 0);

	private Matrix4fc viewMatrix;
	private Matrix4fc rotationMatrix;
	private Matrix4fc projectionMatrix;

	public PerspectiveCamera(float width, float height)
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
		this.projectionMatrix = new Matrix4f().perspective(this.fieldOfView, this.aspectRatio, this.clippingNear, this.clippingFar);
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
