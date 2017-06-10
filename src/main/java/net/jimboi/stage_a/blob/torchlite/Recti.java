package net.jimboi.stage_a.blob.torchlite;

public class Recti
{
	public int x, y, width, height;

	public Recti()
	{
		this.x = this.y = this.width = this.height = 0;
	}

	public Recti(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Recti(Vec2i position, Vec2i dimension)
	{
		this.x = position.x;
		this.y = position.y;
		this.width = dimension.x;
		this.height = dimension.y;
	}

	public Recti inflate(int amount)
	{
		return new Recti(this.x - amount, this.y - amount, this.width + amount * 2, this.height + amount * 2);
	}

	public boolean contains(Vec2f vec)
	{
		return vec.x >= this.x &&
				vec.x <= this.x + this.width &&
				vec.y >= this.y &&
				vec.y <= this.y + this.height;
	}

	public boolean contains(Vec2i vec)
	{
		return vec.x >= this.x &&
				vec.x <= this.x + this.width &&
				vec.y >= this.y &&
				vec.y <= this.y + this.height;
	}

	public boolean intersects(Recti rect)
	{
		return this.x <= rect.x + rect.width &&
				this.x + this.width >= rect.x &&
				this.y <= rect.y + rect.height &&
				this.y + this.height >= rect.y;
	}

	public Vec2i getPosition()
	{
		return new Vec2i(this.x, this.y);
	}

	public Vec2i getDimension()
	{
		return new Vec2i(this.width, this.height);
	}

	@Override
	public String toString()
	{
		return this.getPosition().toString() + this.getDimension().toString();
	}
}
