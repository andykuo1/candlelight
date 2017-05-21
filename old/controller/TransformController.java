package net.jimboi.mod3.controller;

import org.bstone.input.InputManager;
import net.jimboi.mod.transform.Transform3;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class TransformController
{
	public abstract void update(InputManager inputManager, Transform3 transform, double delta);
}
