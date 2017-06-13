package net.jimboi.stage_a.dood;

import net.jimboi.stage_a.dood.component.ComponentBox2DBody;
import net.jimboi.stage_a.dood.component.ComponentInstanceable;
import net.jimboi.stage_a.dood.component.ComponentTransform;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Andy on 5/23/17.
 */
public class EntityBall
{
	public static Entity create(EntityManager entityManager)
	{
		Transform3 transform = Transform3.create();
		transform.position.y = 30;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = transform.position.x;
		bodyDef.position.y = transform.position.y;
		bodyDef.type = BodyType.DYNAMIC;
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(1F);
		fixDef.shape = shape;
		fixDef.density = 1.0F;
		fixDef.friction = 0.3F;

		return entityManager.createEntity()
				.addComponent(new ComponentTransform(transform))
				.addComponent(new ComponentBox2DBody(bodyDef, fixDef))
				.addComponent(new ComponentInstanceable("ball", "bird", "diffuse"));
	}
}