package net.jimboi.stage_c.hoob.bounding;

/**
 * Created by Andy on 7/14/17.
 */
public class ColliderAABB extends ShapeAABB
{
	public ColliderAABB(float x, float y, float radius)
	{
		super(x, y, radius);
	}

	public ColliderAABB(float x, float y, float width, float height)
	{
		super(x, y, width, height);
	}

	public void update(float x, float y)
	{
		this.center.set(x, y);
	}
}
