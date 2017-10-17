package net.jimboi.test.pye;

import java.awt.Color;

/**
 * Created by Andy on 10/17/17.
 */
public class Matter
{
	protected static final float PI = 3.14159F;
	protected static final float PI2 = PI * 2F;
	protected static final float PIHALF = PI / 2F;

	protected float posX;
	protected float posY;
	protected float rotation;
	protected float radius = 1;

	protected float motionX;
	protected float motionY;
	protected float motionRot;

	protected float maxMotion = 1F;
	protected float maxAngular = Matter.PIHALF;

	protected float mass = 1;

	protected float redBody = 0.8F;
	protected float greenBody = 0.8F;
	protected float blueBody = 0.8F;

	private boolean dead = false;

	public Matter(float posX, float posY, float rot)
	{
		this.posX = posX;
		this.posY = posY;
		this.rotation = rot;
	}

	public void onCreate(PetriDish dish)
	{
	}

	public void onDestroy(PetriDish dish)
	{

	}

	public void onUpdate(PetriDish dish)
	{
	}

	public void onCollideWith(Matter matter, float pointX, float pointY)
	{
	}

	public void onMotionUpdate(PetriDish dish)
	{
		float posf = dish.getMotionFriction(this.posX, this.posY);
		float rotf = dish.getAngularFriction();

		if (this.motionX > this.maxMotion) this.motionX = this.maxMotion;
		if (this.motionX < -this.maxMotion) this.motionX = -this.maxMotion;
		if (this.motionY > this.maxMotion) this.motionY = this.maxMotion;
		if (this.motionY < -this.maxMotion) this.motionY = -this.maxMotion;
		if (this.motionRot > this.maxAngular) this.motionRot = this.maxAngular;
		if (this.motionRot < -this.maxAngular) this.motionRot = -this.maxAngular;

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.rotation += this.motionRot;
		this.rotation %= Matter.PI2;

		this.motionX *= posf;
		this.motionY *= posf;
		this.motionRot *= rotf;
	}

	public void onCollisionUpdate(Iterable<Matter> matters)
	{
		for(Matter matter : matters)
		{
			if (matter == this) continue;

			if (this.posX + this.radius + matter.radius > matter.posX
					&& this.posX < matter.posX + this.radius + matter.radius
					&& this.posY + this.radius + matter.radius > matter.posY
					&& this.posY < matter.posY + this.radius + matter.radius)
			{
				float dx = this.posX - matter.posX;
				float dy = this.posY - matter.posY;
				float dist = (float) Math.sqrt((dx * dx) + (dy * dy));

				float size2 = this.radius + matter.radius;
				if (dist < size2)
				{
					//Calculate point of collision
					float pointX = ((this.posX * matter.radius) + (matter.posX * this.radius)) / size2;
					float pointY = ((this.posY * matter.radius) + (matter.posY * this.radius)) / size2;

					//Move to contact point
					float dpx = pointX - this.posX;
					float dpy = pointY - this.posY;
					float dpdir = (float) Math.atan2(-dpy, -dpx);
					this.posX = pointX + (float) Math.cos(dpdir) * this.radius;
					this.posY = pointY + (float) Math.sin(dpdir) * this.radius;

					//Calculate post collision motion
					float mass2 = this.mass + matter.mass;
					float dmass1 = this.mass - matter.mass;
					float dmass2 = matter.mass - this.mass;

					float vx1 = (this.motionX * dmass1 + (2 * matter.mass * matter.motionX)) / mass2;
					float vy1 = (this.motionY * dmass1 + (2 * matter.mass * matter.motionY)) / mass2;

					float vx2 = (matter.motionX * dmass2 + (2 * this.mass * this.motionX)) / mass2;
					float vy2 = (matter.motionY * dmass2 + (2 * this.mass * this.motionY)) / mass2;

					this.motionX = vx1;
					this.motionY = vy1;

					matter.motionX = vx2;
					matter.motionY = vy2;

					this.posX += vx1;
					this.posY += vy1;

					matter.posX += vx2;
					matter.posY += vy2;

					this.onCollideWith(matter, pointX, pointY);
				}
			}
		}
	}

	public void moveToward(float dir, float amt)
	{
		this.motionX += (float) Math.cos(dir) * amt;
		this.motionY += (float) Math.sin(dir) * amt;
	}

	public Color getColor()
	{
		return new Color(
				this.redBody,
				this.greenBody,
				this.blueBody);
	}

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}
}
