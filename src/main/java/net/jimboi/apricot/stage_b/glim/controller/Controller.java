package net.jimboi.apricot.stage_b.glim.controller;

import org.bstone.window.camera.Camera;
import org.bstone.window.input.InputEngine;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Controller
{
	public abstract void poll(InputEngine inputEngine);

	public abstract void update(Camera camera, double delta);
}
