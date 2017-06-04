package org.bstone.camera;

/**
 * Created by Andy on 5/24/17.
 */
public interface CameraController
{
	void onCameraStart(Camera camera);

	void onCameraUpdate(Camera camera, double delta);

	void onCameraStop(Camera camera);
}
