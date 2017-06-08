package net.jimboi.dood;

import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentInstanceable;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.dood.worldgen.WorldGenHills;
import net.jimboi.mod2.transform.Transform3;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

import java.util.List;

/**
 * Created by Andy on 5/23/17.
 */
public class EntityHills
{
	public static Entity create(EntityManager entityManager, WorldGenHills hills)
	{
		Transform3 transform = Transform3.create();
		transform.position.x = -100;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = transform.position.x;
		bodyDef.position.y = transform.position.y;
		bodyDef.type = BodyType.STATIC;
		FixtureDef fixDef = new FixtureDef();
		ChainShape shape = new ChainShape();
		List<Vector3f> vecs = hills.getVertices();
		Vec2[] chain = new Vec2[vecs.size() + 1];
		chain[0] = new Vec2(0, 0);
		for (int i = 1; i < chain.length; ++i)
		{
			Vector3fc vec = vecs.get(i - 1);
			chain[i] = new Vec2(vec.x(), vec.y());
		}
		shape.createChain(chain, chain.length);
		fixDef.shape = shape;
		fixDef.density = 0.0F;
		fixDef.friction = 0.6F;

		return entityManager.createEntity()
				.addComponent(new ComponentTransform(transform))
				.addComponent(new ComponentBox2DBody(bodyDef, fixDef))
				.addComponent(new ComponentInstanceable("hills", "bird", "diffuse"));
	}
}
