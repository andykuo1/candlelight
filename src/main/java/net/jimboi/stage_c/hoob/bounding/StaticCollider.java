package net.jimboi.stage_c.hoob.bounding;

/**
 * Created by Andy on 7/14/17.
 */
public class StaticCollider
{
	protected final BoundingManager boundingManager;
	protected final Shape shape;

	StaticCollider(BoundingManager boundingManager, Shape shape)
	{
		this.boundingManager = boundingManager;
		this.shape = shape;
	}

	public Shape getShape()
	{
		return this.shape;
	}
}
