package net.jimboi.stage_b.glim.entity.component;

import org.bstone.transform.Transform;
import org.joml.Vector3fc;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 6/14/17.
 */
public class EntityComponentHeading implements EntityComponent
{
	public float rotation;
	public float heading;
	public float rotspeed = 8F;

	public float motion;
	public float velocity;
	public float speed;
	public float maxVelocity;
	public float friction = 0.9F;

	public EntityComponentHeading(float speed, float maxSpeed)
	{
		this.speed = speed;
		this.maxVelocity = maxSpeed;
	}

	public void moveForward()
	{
		this.motion = 1;
	}

	public void moveBackward()
	{
		this.motion = -1;
	}

	public void moveDirection(Vector3fc dir)
	{
		this.heading = (float)-Math.atan2(dir.z(), -dir.x());
		this.motion = 1;
	}

	public void stop()
	{
		if (this.motion != 0)
		{
			this.motion = 0;
		}
	}

	public void lookDirection(float direction)
	{
		this.heading = direction % Transform.PI2;
	}
}
