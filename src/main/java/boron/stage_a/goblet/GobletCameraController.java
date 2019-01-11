package boron.stage_a.goblet;

import boron.bstone.camera.Camera;
import boron.bstone.camera.CameraController;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletCameraController extends CameraController
{
	@Override
	protected void onCameraStart(Camera camera)
	{

	}

	@Override
	protected boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta)
	{
		if (this.target != null)
		{
			cameraTransform.position.lerp(this.target.position, 0.02F);
			cameraTransform.position.z = 0;
		}

		return true;
	}

	@Override
	protected void onCameraStop(Camera camera)
	{

	}
}
