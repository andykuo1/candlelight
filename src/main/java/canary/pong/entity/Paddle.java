package canary.pong.entity;

import canary.pong.component.ComponentRenderable;
import canary.pong.component.ComponentTransform;

import canary.bstone.entity.Entity;

/**
 * Created by Andy on 12/2/17.
 */
public class Paddle extends Entity
{
	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentTransform());
		this.addComponent(new ComponentRenderable("paddle"));
	}
}
