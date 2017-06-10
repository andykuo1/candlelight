package net.jimboi.stage_b.glim.controller;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Controller
{
	public abstract void poll(InputEngine inputEngine);

	public abstract void update(Camera camera, double delta);
}
