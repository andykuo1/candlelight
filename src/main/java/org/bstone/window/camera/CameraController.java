package org.bstone.window.camera;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 5/24/17.
 */
public abstract class CameraController
{
	private Camera camera;

	protected Transform3 target;

	public CameraController setTarget(Transform3 target)
	{
		this.target = target;
		return this;
	}

	public final void start(Camera camera)
	{
		this.camera = camera;
		this.onCameraStart(this.camera);
	}

	public final void update(double delta)
	{
		boolean dirty = this.onCameraUpdate(this.camera, this.camera.transform, delta);
		if (dirty)
		{
			this.camera.markDirty();
		}
	}

	public final void stop()
	{
		this.onCameraStop(this.camera);
		this.camera = null;
	}

	protected abstract void onCameraStart(Camera camera);

	protected abstract boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta);

	protected abstract void onCameraStop(Camera camera);
}
