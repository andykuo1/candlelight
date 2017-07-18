package net.jimboi.apricot.stage_a.blob.torchlite;

import java.util.Iterator;

/**
 * Iterates from passed-in vector to vector; inclusive
 * */
public class Vec2Iterator implements Iterator<Vec2i>
{
	private int x;
	private int y;

	private final Vec2i from;
	private final Vec2i to;

	public Vec2Iterator(Vec2i from, Vec2i to)
	{
		Log.ASSERT(from != null);
		Log.ASSERT(to != null);
		Log.ASSERT(from.sub(to).x <= 0);
		Log.ASSERT(from.sub(to).y <= 0);

		this.from = from.copy();
		this.to = to.copy();

		this.x = this.from.x;
		this.y = this.from.y;
	}

	public Vec2Iterator(Recti rect)
	{
		this(rect.getPosition(), rect.getPosition().add(rect.getDimension()));
	}

	@Override
	public boolean hasNext()
	{
		return this.x <= this.to.x && this.y <= this.to.y;
	}

	@Override
	public Vec2i next()
	{
		Vec2i pos = new Vec2i(this.x, this.y);
		if (++this.x > this.to.x)
		{
			this.x = this.from.x;
			++this.y;
		}

		return pos;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Cannot remove position");
	}
}
