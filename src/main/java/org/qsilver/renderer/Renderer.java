package org.qsilver.renderer;

import org.bstone.camera.Camera;

import java.util.Iterator;

/**
 * Created by Andy on 7/6/17.
 */
public abstract class Renderer
{
	public abstract void render(Camera camera, Iterator<Renderable> renderables);
}
