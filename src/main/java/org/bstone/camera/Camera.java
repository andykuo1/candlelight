package org.bstone.camera;

import net.jimboi.mod.transform.Transform3;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Camera
{
	private CameraController controller;

	protected final Matrix4f viewMatrix = new Matrix4f();
	protected final Matrix4f rotationMatrix = new Matrix4f();
	protected final Matrix4f projectionMatrix = new Matrix4f();

	public Camera()
	{
	}

	public void update(double delta)
	{
		if (this.controller != null)
		{
			this.controller.onCameraUpdate(this, delta);
		}
	}

	public final CameraController setCameraController(CameraController controller)
	{
		if (this.controller == controller) return controller;
		CameraController ret = this.controller;
		if (ret != null)
		{
			ret.onCameraStop(this);
		}
		this.controller = controller;
		this.controller.onCameraStart(this);
		return ret;
	}

	public final CameraController getCameraController()
	{
		return this.controller;
	}

	public final boolean isCurrentCameraController(CameraController controller)
	{
		return this.controller == controller;
	}

	public abstract Transform3 getTransform();

	public final Matrix4fc orientation()
	{
		this.updateRotationMatrix(this.rotationMatrix.identity());
		return this.rotationMatrix;
	}

	public final Matrix4fc projection()
	{
		this.updateProjectionMatrix(this.projectionMatrix.identity());
		return this.projectionMatrix;
	}

	public final Matrix4fc view()
	{
		this.updateViewMatrix(this.viewMatrix.identity());
		return this.viewMatrix;
	}

	protected abstract void updateProjectionMatrix(Matrix4f mat);

	protected abstract void updateRotationMatrix(Matrix4f mat);

	protected abstract void updateViewMatrix(Matrix4f mat);
}
