package org.bstone.camera;

import org.bstone.input.InputEngine;

/**
 * Created by Andy on 5/24/17.
 */
public interface CameraController
{
	void onCameraStart(Camera camera);

	void onCameraUpdate(Camera camera, InputEngine inputEngine);

	void onCameraStop(Camera camera);
}
