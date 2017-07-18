package net.jimboi.apricot.stage_b.glim.bounding.square;

import org.joml.Vector2f;

/**
 * Created by Andy on 6/4/17.
 */
public class Segment extends Bounding2D
{
	public final Vector2f length = new Vector2f();

	public Segment(float centerX, float centerY, float dx, float dy)
	{
		super(centerX, centerY);

		this.length.set(dx, dy);
	}
}
