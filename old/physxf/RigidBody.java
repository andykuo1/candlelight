package net.jimboi.physx;

/**
 * Created by Andy on 5/17/17.
 */
public class RigidBody
{
	public int posX;
	public int posY;

	public int velocityX;
	public int velocityY;

	public int restitution;
	public int mass;
	public int inv_mass;

	AABB aabb;
	Circle circle;

	public RigidBody(int velocityX, int velocityY, int restitution, int mass)
	{
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.restitution = restitution;
		this.mass = mass;

		if (this.mass == 0)
		{
			this.mass = 0;
		}
		else
		{
			this.inv_mass = Mathf.inv(this.mass);
		}
	}
}
