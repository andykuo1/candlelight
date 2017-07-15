package net.jimboi.stage_c.hoob.bounding;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/14/17.
 */
public class BoundingManager
{
	public Set<Shape> shapes = new HashSet<>();

	public Collider createCollider(BoundingShape shape)
	{
		this.shapes.add(shape);
		return new Collider(this, shape);
	}

	public void destroyCollider(Collider collider)
	{
		this.shapes.remove(collider.getShape());
	}

	public StaticCollider createStatic(Shape shape)
	{
		this.shapes.add(shape);
		return new StaticCollider(this, shape);
	}

	public void destryStatic(StaticCollider collider)
	{
		this.shapes.remove(collider.getShape());
	}

	public Iterator<Shape> getShapeIterator()
	{
		return this.shapes.iterator();
	}
}
