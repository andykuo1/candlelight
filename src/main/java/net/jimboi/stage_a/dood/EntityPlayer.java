package net.jimboi.stage_a.dood;

import net.jimboi.stage_a.dood.component.ComponentBox2DBody;
import net.jimboi.stage_a.dood.component.ComponentInstanceable;
import net.jimboi.stage_a.dood.component.ComponentSpriteSheet;
import net.jimboi.stage_a.dood.component.ComponentTransform;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;
import net.jimboi.stage_a.mod.sprite.SpriteSheet;
import net.jimboi.stage_a.mod.sprite.TextureAtlas;
import net.jimboi.stage_a.mod.sprite.TiledTextureAtlas;

import org.bstone.transform.Transform3;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Andy on 5/22/17.
 */
public class EntityPlayer
{
	public static Entity create(EntityManager entityManager)
	{
		TextureAtlas textureAtlas = new TiledTextureAtlas(Resources.getMaterial("font").texture, 16, 16);
		SpriteSheet spriteSheet = textureAtlas.createSpriteSheet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81);

		Transform3 transform = new Transform3();
		transform.position.y = 30;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = transform.position.x;
		bodyDef.position.y = transform.position.y;
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.fixedRotation = true;
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.4F);
		/*
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5F, 0.5F);
		*/
		fixDef.shape = shape;
		fixDef.density = 1.0F;
		fixDef.friction = 0.3F;
		return entityManager.createEntity()
				.addComponent(new ComponentTransform(transform))
				.addComponent(new ComponentBox2DBody(bodyDef, fixDef))
				.addComponent(new ComponentSpriteSheet("font", spriteSheet))
				.addComponent(new ComponentInstanceable("box", "font", "diffuse"));
	}
}
