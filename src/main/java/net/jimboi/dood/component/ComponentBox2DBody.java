package net.jimboi.dood.component;

import net.jimboi.dood.base.EntityBox2DHandler;
import net.jimboi.mod.Box2DHandler;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentBox2DBody extends Component
{
	public final Box2DHandler handler;

	public ComponentBox2DBody(BodyDef bodyDef, FixtureDef fixtureDef)
	{
		this.handler = new EntityBox2DHandler(bodyDef, fixtureDef);
	}
}
