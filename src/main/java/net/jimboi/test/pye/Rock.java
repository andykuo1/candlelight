package net.jimboi.test.pye;

/**
 * Created by Andy on 10/15/17.
 */
public class Rock extends GameEntity
{
	public Rock(float x, float y)
	{
		super(x, y);

		this.size = (float) Math.random() * 50 + 10;
		this.dir = (float) (Math.random() * Math.PI * 2);
	}
}
