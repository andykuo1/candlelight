package net.jimboi.boron.stage_a.goblet.entity;

/**
 * Created by Andy on 8/11/17.
 */
public interface IBurnable
{
	boolean canSetFire(float x, float y, float strength);
	void setOnFire(float strength);
	void extinguish();

	boolean isOnFire();
}
