package net.jimboi.mod3;

import net.jimboi.mod.entity.ComponentHandler;

/**
 * Created by Andy on 5/20/17.
 */
public class EntityZombie extends Entity3D
{
	public EntityZombie(float x, float y, float z)
	{
		super(x, y, z);
	}

	@Override
	public void onComponentSetup(ComponentHandler componentHandler)
	{

	}

	@Override
	public String getModelID()
	{
		return "plane";
	}

	@Override
	public String getMaterialID()
	{
		return "billboard";
	}
}
