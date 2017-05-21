package org.bstone.camera;

import net.jimboi.mod.transform.Transform;

import org.joml.Matrix4fc;

/**
 * Created by Andy on 5/19/17.
 */
public interface Camera
{
	Transform getTransform();

	Matrix4fc orientation();
	Matrix4fc projection();
	Matrix4fc view();
}
