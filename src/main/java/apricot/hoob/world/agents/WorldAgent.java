package apricot.hoob.world.agents;

import apricot.base.entity.EntityComponent;
import apricot.base.living.OldLiving;
import apricot.hoob.world.World;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class WorldAgent extends OldLiving implements EntityComponent
{
	public final World world;
	private final float size;
	public final Vector2f pos = new Vector2f();

	public WorldAgent(World world, float size)
	{
		this.world = world;
		this.size = size;
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	public boolean isSolid()
	{
		return this.size > 0;
	}

	public float getSize()
	{
		return this.size;
	}
}
