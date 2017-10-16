package org.bstone.tick;

/**
 * Created by Andy on 10/12/17.
 */
public interface TickHandler
{
	default void onFirstUpdate() {}
	default void onEarlyUpdate() {}
	void onFixedUpdate();
	default void onLateUpdate() {}
	default void onLastUpdate() {}
}
