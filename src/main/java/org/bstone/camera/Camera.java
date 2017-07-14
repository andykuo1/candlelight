package org.bstone.camera;

import org.bstone.util.listener.Listenable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.qsilver.transform.Transform;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Camera
{
	public interface OnCameraChangedListener
	{
		void onCameraChanged(Camera camera);
	}

	public final Listenable<OnCameraChangedListener> onCameraChanged = new Listenable<>((listener, objects) -> listener.onCameraChanged((Camera) objects[0]));

	private CameraController controller;
	private boolean dirty;

	protected final Matrix4f viewMatrix = new Matrix4f();
	protected final Matrix4f rotationMatrix = new Matrix4f();
	protected final Matrix4f projectionMatrix = new Matrix4f();

	public Camera()
	{
		this.dirty = true;
	}

	public void update(double delta)
	{
		if (this.controller != null)
		{
			if (this.controller.onCameraUpdate(this, (Transform3) this.getTransform(), delta))
			{
				this.dirty = true;
			}
		}

		if (this.dirty) this.updateCamera();
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

	public abstract Transform getTransform();

	public final Matrix4fc orientation()
	{
		if (this.dirty) this.updateCamera();
		return this.rotationMatrix;
	}

	public final Matrix4fc projection()
	{
		if (this.dirty) this.updateCamera();
		return this.projectionMatrix;
	}

	public final Matrix4fc view()
	{
		if (this.dirty) this.updateCamera();
		return this.viewMatrix;
	}

	private void updateCamera()
	{
		this.updateProjectionMatrix(this.projectionMatrix.identity());
		this.updateViewMatrix(this.viewMatrix.identity());
		this.updateRotationMatrix(this.rotationMatrix.identity());

		this.dirty = false;
		this.onCameraChanged.notifyListeners(this);
	}

	protected abstract void updateProjectionMatrix(Matrix4f mat);

	protected abstract void updateRotationMatrix(Matrix4f mat);

	protected abstract void updateViewMatrix(Matrix4f mat);

	public final boolean isDirty()
	{
		return this.dirty;
	}

	public final void markDirty()
	{
		this.dirty = true;
	}
}
