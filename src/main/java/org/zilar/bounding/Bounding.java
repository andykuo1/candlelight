package org.zilar.bounding;

import java.util.Iterator;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class Bounding
{
	protected final BoundingManager boundingManager;
	protected final Shape shape;

	Bounding(BoundingManager boundingManager, Shape shape)
	{
		this.boundingManager = boundingManager;
		this.shape = shape;
	}

	public IntersectionData checkIntersection()
	{
		Iterator<Shape> iter = this.boundingManager.getShapeIterator();
		while (iter.hasNext())
		{
			Shape other = iter.next();
			if (other == this.shape) continue;

			IntersectionData intersection = Intersection.checkIntersection(this.shape, other);
			if (intersection != null) return intersection;
		}

		return null;
	}

	public final Shape getShape()
	{
		return this.shape;
	}
}
