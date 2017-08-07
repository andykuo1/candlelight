package net.jimboi.boron.stage_a.smack;

import org.bstone.transform.Transform3;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.CameraController;

/**
 * Created by Andy on 8/6/17.
 */
public class FollowCameraController extends CameraController
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
