package net.jimboi.canary.stage_a.lantern.newglim.entity.component;

import org.joml.Vector3fc;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 6/14/17.
 */
public class EntityComponentTargeter implements EntityComponent
{
	public Vector3fc target;
	public float nearestDist;

	public EntityComponentTargeter(float nearestDist)
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
