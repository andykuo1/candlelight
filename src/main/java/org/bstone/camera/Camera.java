package org.bstone.camera;

import net.jimboi.mod.transform.Transform3;

import org.joml.Matrix4fc;

/**
 * Created by Andy on 5/19/17.
 */
public interface Camera
{
	Transform3 getTransform();

	Matrix4fc orientation();
	Matrix4fc projection();
	Matrix4fc view();
}
