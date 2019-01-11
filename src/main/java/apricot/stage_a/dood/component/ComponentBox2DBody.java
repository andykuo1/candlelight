package apricot.stage_a.dood.component;

import apricot.stage_a.dood.Box2DHandler;
import apricot.stage_a.dood.entity.Component;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentBox2DBody extends Component implements Box2DHandler
{
	protected final BodyDef bodyDef;
	protected final FixtureDef fixtureDef;

	protected Body body;
	protected Fixture fixture;

	public ComponentBox2DBody(BodyDef bodyDef, FixtureDef fixtureDef)
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
