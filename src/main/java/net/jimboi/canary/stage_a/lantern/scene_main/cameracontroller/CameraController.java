package net.jimboi.canary.stage_a.lantern.scene_main.cameracontroller;

import org.bstone.camera.Camera;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 11/3/17.
 */
public abstract class CameraController
{
	private final Camera camera;

	public CameraController(Camera camera)
	{
		this.camera = camera;
	}

	public final void update()
	{
		if (this.onUpdate(this.camera, (Transform3) this.camera.transform()))
		{
			this.camera.markDirty();
		}
	}

	protected abstract boolean onUpdate(Camera camera, Transform3 cameraTransform);

	public final Camera getCamera()
	{
		return this.camera;
	}
}
