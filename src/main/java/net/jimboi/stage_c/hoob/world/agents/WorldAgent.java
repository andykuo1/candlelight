package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.world.World;

import org.bstone.living.Living;
import org.joml.Vector2f;
import org.qsilver.entity.EntityComponent;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class WorldAgent extends Living implements EntityComponent
{
	public final World world;
	public final Vector2f pos = new Vector2f();

	public WorldAgent(World world)
	{
		this.world = world;
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}
}
