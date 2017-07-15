package net.jimboi.stage_c.hoob;

import org.qsilver.entity.EntityComponent;
import org.zilar.bounding.BoundingStatic;
import org.zilar.bounding.Shape;

/**
 * Created by Andy on 7/15/17.
 */
public class EntityComponentBoundingStatic implements EntityComponent
{
	protected BoundingStatic bounding;
	public Shape shape;

	public EntityComponentBoundingStatic(Shape shape)
	{
		this.shape = shape;
	}

	public BoundingStatic getBounding()
	{
		return this.bounding;
	}
}
