package org.bstone.camera;

import org.zilar.transform.Transform3;

/**
 * Created by Andy on 5/24/17.
 */
public interface CameraController
{
	void onCameraStart(Camera camera);

	boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta);

	void onCameraStop(Camera camera);
}
