package org.qsilver.renderer;

import org.joml.Matrix4f;
import org.zilar.model.Model;

/**
 * Created by Andy on 6/27/17.
 */
public interface Drawable
{
	Model getModel();
	Matrix4f getRenderTransformation(Matrix4f dst);
	boolean isVisible();
}
