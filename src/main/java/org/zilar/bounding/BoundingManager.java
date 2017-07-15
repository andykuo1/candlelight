package org.zilar.bounding;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/14/17.
 */
public class BoundingManager
{
	private final Set<Shape> shapes = new HashSet<>();

	public BoundingManager()
	{
	}

	public BoundingCollider createCollider(Shape shape)
	{
		this.shapes.add(shape);
		return new BoundingCollider(this, shape);
	}

	public void destroyCollider(BoundingCollider collider)
	{
		this.shapes.remove(collider.getShape());
	}

	public BoundingStatic createStatic(Shape shape)
	{
		this.shapes.add(shape);
		return new BoundingStatic(this, shape);
	}

	public void destroyStatic(BoundingStatic bounding)
	{
		this.shapes.remove(bounding.getShape());
	}

	public void clear()
	{
		this.shapes.clear();
	}

	public Iterator<Shape> getShapeIterator()
	{
		return this.shapes.iterator();
	}
}
