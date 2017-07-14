package org.bstone.camera;

import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.bstone.util.listener.Listenable;
import org.bstone.window.ViewPort;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

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

	protected final Transform3 transform;
	protected final Matrix4f viewMatrix = new Matrix4f();
	protected final Matrix4f rotationMatrix = new Matrix4f();
	protected final Matrix4f projectionMatrix = new Matrix4f();

	public Camera(Transform3 transform)
	{
		this.transform = transform;
		this.dirty = true;
	}

	public void update(double delta)
	{
		if (this.controller != null)
		{
			if (this.controller.onCameraUpdate(this, (Transform3) this.transform(), delta))
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

	public final Transform3c transform()
	{
		return this.transform;
	}

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

	public static Vector3f getWorld2DFromScreen(Camera camera, ViewPort viewport, float screenX, float screenY, Vector3f dst)
	{
		Matrix4fc view = camera.view();
		Matrix4fc projection = camera.projection();
		Matrix4f invertedViewProjection = projection.mul(view, new Matrix4f()).invert();

		screenY = viewport.getHeight() - screenY;

		Vector3f near = unproject(invertedViewProjection, viewport, screenX, screenY, 0, new Vector3f());
		Vector3f far = unproject(invertedViewProjection, viewport, screenX, screenY, 1, new Vector3f());

		float f = (0 - near.z) / (far.z - near.z);
		screenX = (near.x + f * (far.x - near.x));
		screenY = (near.y + f * (far.y - near.y));
		return dst.set(screenX, screenY, 0);
	}

	public static Vector3f getWorldFromScreen(Camera camera, ViewPort viewport, float screenX, float screenY, float screenDepth, Vector3f dst)
	{
		Matrix4fc view = camera.view();
		Matrix4fc projection = camera.projection();
		Matrix4f invertedViewProjection = projection.mul(view, new Matrix4f()).invert();

		screenY = viewport.getHeight() - screenY;

		return unproject(invertedViewProjection, viewport, screenX, screenY, screenDepth, dst);
	}

	protected static Vector3f unproject(Matrix4fc invertedViewProjection, ViewPort viewport, float screenX, float screenY, float z, Vector3f dst)
	{
		Vector4f normalizedDeviceCoords = new Vector4f();
		normalizedDeviceCoords.x = (screenX - viewport.getX()) / viewport.getWidth() * 2.0F - 1.0F;
		normalizedDeviceCoords.y = (screenY - viewport.getY()) / viewport.getHeight() * 2.0F - 1.0F;
		normalizedDeviceCoords.z = 2.0F * z - 1.0F;
		normalizedDeviceCoords.w = 1.0F;

		Vector4f objectCoords = invertedViewProjection.transform(normalizedDeviceCoords);
		if (objectCoords.w != 0.0F) objectCoords.w = 1.0F / objectCoords.w;

		return dst.set(objectCoords.x, objectCoords.y, objectCoords.z).mul(objectCoords.w);
	}
}
