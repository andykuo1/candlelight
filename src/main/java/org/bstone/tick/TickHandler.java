package org.bstone.tick;

/**
 * Created by Andy on 8/1/17.
 */
public interface TickHandler
{
	void onFirstUpdate(TickEngine tickEngine);
	void onPreUpdate();
	void onFixedUpdate();
	void onUpdate(double delta);
	void onLastUpdate(TickEngine tickEngine);
}
