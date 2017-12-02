package net.jimboi.canary.stage_a.pong.entity;

import net.jimboi.canary.stage_a.pong.component.ComponentRenderable;
import net.jimboi.canary.stage_a.pong.component.ComponentTransform;

import org.bstone.entity.Entity;

/**
 * Created by Andy on 12/1/17.
 */
public class Ball extends Entity
{
	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentTransform());
		this.addComponent(new ComponentRenderable("ball"));
	}
}
