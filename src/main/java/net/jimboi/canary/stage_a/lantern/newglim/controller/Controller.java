package net.jimboi.canary.stage_a.lantern.newglim.controller;

import net.jimboi.boron.base_ab.window.input.InputEngine;

import org.bstone.camera.Camera;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Controller
{
	public abstract void poll(InputEngine inputEngine);

	public abstract void update(Camera camera, double delta);
}
