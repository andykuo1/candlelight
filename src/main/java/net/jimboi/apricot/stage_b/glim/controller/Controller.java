package net.jimboi.apricot.stage_b.glim.controller;

import net.jimboi.boron.base.window.input.InputEngine;

import org.bstone.camera.Camera;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Controller
{
	public abstract void poll(InputEngine inputEngine);

	public abstract void update(Camera camera, double delta);
}
