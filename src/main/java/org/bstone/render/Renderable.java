package org.bstone.render;

/**
 * Created by Andy on 1/17/18.
 */
public interface Renderable
{
	default void load() {}
	default void unload() {}

	void render();
}
