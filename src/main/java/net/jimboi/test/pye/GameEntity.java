package net.jimboi.test.pye;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by Andy on 10/15/17.
 */
public class GameEntity extends GameObject
{
	public Color color = Color.WHITE;
	public float x = 0;
	public float y = 0;
	public float dir = 0;

	public float vspeed = 0;
	public float hspeed = 0;
	public float rotspeed = 0;

	public float size = 8;

	public boolean solid = true;

	public GameEntity(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public void onUpdate()
	{
		this.dir += this.rotspeed;
		this.dir %= Math.PI * 2;

		float vr = this.dir;
		float vx = (float) Math.cos(vr) * this.vspeed;
		float vy = (float) Math.sin(vr) * this.vspeed;

		float hr = (float) (this.dir + Math.PI / 2F);
		float hx = (float) Math.cos(hr) * this.hspeed;
		float hy = (float) Math.sin(hr) * this.hspeed;

		this.x += vx + hx;
		this.y += vy + hy;

		this.world.solveCollision(this);

		super.onUpdate();
	}

	@Override
	public void onRender(Graphics g)
	{
		super.onRender(g);

		g.setColor(this.color);
		g.fillOval((int) (this.x - this.size), (int) (this.y - this.size),
				(int) (this.size * 2),
				(int) (this.size * 2));

		g.setColor(Color.BLACK);
		g.drawOval((int) (this.x - this.size), (int) (this.y - this.size),
				(int) (this.size * 2),
				(int) (this.size * 2));
		g.drawLine((int) this.x, (int) this.y,
				(int) (this.x + Math.cos(this.dir) * this.size),
				(int) (this.y + Math.sin(this.dir) * this.size));
	}
}
