package net.jimboi.stage_a.dood;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Created by Andy on 5/24/17.
 */
public interface Box2DHandler
{
	void create(World world);

	void destroy(World world);

	Body getBody();

	Fixture getFixture();

	BodyDef getBodyDef();

	FixtureDef getFixtureDef();
}
