package net.jimboi.stage_b.glim.gameentity.component;

import org.joml.Vector3fc;

/**
 * Created by Andy on 6/14/17.
 */
public class GameComponentTargeter extends GameComponent
{
	public Vector3fc target;
	public float nearestDist;

	public GameComponentTargeter(float nearestDist)
	{
		this.nearestDist = nearestDist;
	}

	public boolean isNearTarget(Vector3fc pos)
	{
		if (this.target == null) return false;
		return pos.distance(this.target) <= this.nearestDist;
	}

	public void setTarget(Vector3fc target)
	{
		this.target = target;
	}
}
