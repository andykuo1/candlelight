package net.jimboi.dood.component;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentBox2DBody extends Component
{
	public final BodyDef bodyDef;
	public final FixtureDef fixtureDef;
	public Body body;

	public ComponentBox2DBody(BodyDef bodyDef, FixtureDef fixtureDef)
	{
		this.bodyDef = bodyDef;
		this.fixtureDef = fixtureDef;
	}
}
