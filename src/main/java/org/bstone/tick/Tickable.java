package org.bstone.tick;

/**
 * Created by Andy on 1/15/18.
 */
public interface Tickable
{
	default void start() {}
	default void stop() {}

	void tick();
}
