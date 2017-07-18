package net.jimboi.apricot.stage_b.glim.bounding.square;

/**
 * Created by Andy on 6/3/17.
 */
public class Circle extends Bounding2D
{
	public float radius = 0F;

	public Circle(float centerX, float centerY, float radius)
	{
		super(centerX, centerY);
		this.radius = radius;
	}
}
