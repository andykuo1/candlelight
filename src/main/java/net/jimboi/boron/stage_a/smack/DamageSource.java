package net.jimboi.boron.stage_a.smack;

/**
 * Created by Andy on 8/6/17.
 */
public class DamageSource
{
	private final SmackEntity entity;

	public DamageSource(SmackEntity entity)
	{
		this.entity = entity;
	}

	public SmackEntity getEntity()
	{
		return this.entity;
	}
}
