package net.jimboi.boron.stage_a.smack;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.transform.Transform3;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/6/17.
 */
public class FollowCameraController extends CameraController
{
	private float maxZoomLevel = 100F;
	private float minZoomLevel = 0.1F;
	private float zoomLevel = 20;

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
			//cameraTransform.position.add(0, -0.2F, 0);
			//cameraTransform.rotation.rotationX(-Transform.HALF_PI / 2F);
			cameraTransform.position.z = this.zoomLevel;
		}

		this.zoomLevel += Smack.getSmack().getInput().getInputMotion("zoom");
		Smack.getSmack().getInput().consumeInput("zoom");
		this.zoomLevel = MathUtil.clamp(this.zoomLevel, minZoomLevel, maxZoomLevel);

		return true;
	}

	@Override
	protected void onCameraStop(Camera camera)
	{

	}
}
