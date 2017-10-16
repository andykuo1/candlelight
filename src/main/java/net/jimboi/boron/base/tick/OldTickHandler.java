package net.jimboi.boron.base.tick;

/**
 * Created by Andy on 8/1/17.
 */
public interface OldTickHandler
{
	void onFirstUpdate(OldTickEngine tickEngine);
	void onPreUpdate();
	void onFixedUpdate();
	void onUpdate(double delta);
	void onLastUpdate(OldTickEngine tickEngine);
}
