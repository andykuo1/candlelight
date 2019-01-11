package canary.lantern.scene_main.cameracontroller;

import canary.bstone.camera.Camera;
import canary.bstone.input.InputEngine;
import canary.bstone.transform.Transform3;

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

	public final void update(InputEngine inputEngine)
	{
		if (this.onUpdate(this.camera, (Transform3) this.camera.transform(), inputEngine))
		{
			this.camera.markDirty();
		}
	}

	protected abstract boolean onUpdate(Camera camera, Transform3 cameraTransform, InputEngine inputEngine);

	public final Camera getCamera()
	{
		return this.camera;
	}
}
