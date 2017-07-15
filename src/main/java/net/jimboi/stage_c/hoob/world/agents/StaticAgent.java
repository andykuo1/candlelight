package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.bounding.StaticCollider;
import net.jimboi.stage_c.hoob.world.World;

/**
 * Created by Andy on 7/14/17.
 */
public class StaticAgent extends WorldAgent
{
	public StaticCollider collider;

	public StaticAgent(World world, StaticCollider collider)
	{
		super(world);

		this.collider = collider;
	}

	public StaticCollider getCollider()
	{
		return this.collider;
	}
}
