package net.jimboi.dood.base;

import net.jimboi.mod.Box2DHandler;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Created by Andy on 5/24/17.
 */
public class EntityBox2DHandler implements Box2DHandler
{
	protected final BodyDef bodyDef;
	protected final FixtureDef fixtureDef;

	protected Body body;
	protected Fixture fixture;

	public EntityBox2DHandler(BodyDef bodyDef, FixtureDef fixtureDef)
	{
		this.bodyDef = bodyDef;
		this.fixtureDef = fixtureDef;
	}

	@Override
	public void create(World world)
	{
		this.body = world.createBody(this.bodyDef);
		this.fixture = this.body.createFixture(this.fixtureDef);
	}

	@Override
	public void destroy(World world)
	{
		this.body.destroyFixture(this.fixture);
		world.destroyBody(this.body);
	}

	@Override
	public Body getBody()
	{
		return this.body;
	}

	@Override
	public Fixture getFixture()
	{
		return this.fixture;
	}

	@Override
	public BodyDef getBodyDef()
	{
		return this.bodyDef;
	}

	@Override
	public FixtureDef getFixtureDef()
	{
		return this.fixtureDef;
	}
}
