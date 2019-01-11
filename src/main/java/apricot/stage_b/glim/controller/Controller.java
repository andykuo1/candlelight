package apricot.stage_b.glim.controller;

import apricot.base.window.input.InputEngine;

import apricot.bstone.camera.Camera;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Controller
{
	public abstract void poll(InputEngine inputEngine);

	public abstract void update(Camera camera, double delta);
}
