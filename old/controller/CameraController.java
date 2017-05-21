package net.jimboi.mod3.controller;

import org.bstone.camera.PerspectiveCamera;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class CameraController extends TransformController
{
	protected final PerspectiveCamera camera;

	public CameraController(PerspectiveCamera camera)
	{
		this.camera = camera;
	}

	public PerspectiveCamera getCamera()
	{
		return this.camera;
	}
}
