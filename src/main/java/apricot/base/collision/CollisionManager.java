package apricot.base.collision;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/20/17.
 */
public class CollisionManager
{
	private Set<Shape> shapes = new HashSet<>();
	private CollisionSolver solver = new CollisionSolver(this);

	public DynamicCollider createCollider(Shape shape)
	{
		this.shapes.add(shape);
		return new DynamicCollider(this, shape);
	}

	public void destroyCollider(DynamicCollider collider)
	{
		this.shapes.remove(collider.getShape());
		collider.collisionManager = null;
	}

	public StaticCollider createStatic(Shape shape)
	{
		this.shapes.add(shape);
		return new StaticCollider(this, shape);
	}

	public void destroyStatic(StaticCollider collider)
	{
		this.shapes.remove(collider.getShape());
		collider.collisionManager = null;
	}

	public CollisionSolver getCollisionSolver()
	{
		return this.solver;
	}

	public Iterator<Shape> getShapes()
	{
		return this.shapes.iterator();
	}
}
