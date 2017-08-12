package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.Goblet;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;

import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityPlayer extends EntityMotion implements ActiveBoxCollider
{
	private Vector2f direction = new Vector2f();
	private Vector2f mousePos = new Vector2f();

	private float speed;

	private float rollSpeed = 0.25F;
	private boolean rolling;

	private int maxRollingTicks = 6;
	private int rollingTicks = 0;

	private int maxRollingInputTicks = 12;
	private int maxRollingCooldownTicks = 15;
	private int rollingInputTicks = 0;
	private int rollingInputDir = -1;

	public EntityPlayer(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.6F),
				world.createRenderable2D(transform, '@', 0xFF00FF));

		this.speed = 0.1F;
		this.friction = 0.2F;
	}

	private void roll(float dx, float dy)
	{
		this.rolling = true;
		this.rollingTicks = this.maxRollingTicks;
		this.motion.set(dx, dy).mul(this.rollSpeed);
	}

	private void updateRoll()
	{
		if (this.rolling)
		{
			//Control the roll
			--this.rollingTicks;
			if (this.rollingTicks <= 0)
			{
				this.rolling = false;
				this.rollingTicks = 0;
				this.rollingInputTicks = -this.maxRollingCooldownTicks;
				this.rollingInputDir = -1;
			}
		}
		else
		{
			//Try to start a roll
			if (this.rollingInputTicks >= this.maxRollingInputTicks)
			{
				this.rollingInputTicks = -1;
				this.rollingInputDir = -1;
			}
			else if (this.rollingInputTicks >= 0 || this.rollingInputTicks < -1)
			{
				++this.rollingInputTicks;
			}

			if (Goblet.getGoblet().getInput().isInputPressed("left"))
			{
				if (this.rollingInputTicks == -1 && this.rollingInputDir != 0)
				{
					this.rollingInputTicks = 0;
					this.rollingInputDir = 0;
				}
				else if (this.rollingInputDir == 0 && this.rollingInputTicks >= 0 && this.rollingInputTicks < this.maxRollingInputTicks)
				{
					this.roll(-1, 0);
				}
			}

			if (Goblet.getGoblet().getInput().isInputPressed("right"))
			{
				if (this.rollingInputTicks == -1 && this.rollingInputDir != 1)
				{
					this.rollingInputTicks = 0;
					this.rollingInputDir = 1;
				}
				else if (this.rollingInputDir == 1 && this.rollingInputTicks >= 0 && this.rollingInputTicks < this.maxRollingInputTicks)
				{
					this.roll(1, 0);
				}
			}

			if (Goblet.getGoblet().getInput().isInputPressed("up"))
			{
				if (this.rollingInputTicks == -1 && this.rollingInputDir != 2)
				{
					this.rollingInputTicks = 0;
					this.rollingInputDir = 2;
				}
				else if (this.rollingInputDir == 2 && this.rollingInputTicks >= 0 && this.rollingInputTicks < this.maxRollingInputTicks)
				{
					this.roll(0, 1);
				}
			}

			if (Goblet.getGoblet().getInput().isInputPressed("down"))
			{
				if (this.rollingInputTicks == -1 && this.rollingInputDir != 3)
				{
					this.rollingInputTicks = 0;
					this.rollingInputDir = 3;
				}
				else if (this.rollingInputDir == 3 && this.rollingInputTicks >= 0 && this.rollingInputTicks < this.maxRollingInputTicks)
				{
					this.roll(0, -1);
				}
			}
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		float mousex = Goblet.getGoblet().getInput().getInputAmount("mousex");
		float mousey = Goblet.getGoblet().getInput().getInputAmount("mousey");
		Vector2fc mouse = Goblet.getGoblet().getRender().getScreenSpace().getPoint2DFromScreen(mousex, mousey, this.mousePos);
		mouse.sub(this.transform.posX(), this.transform.posY(), this.direction);
		this.transform.rotation.rotationZ((float) Math.atan2(this.direction.y(), this.direction.x()));

		this.updateRoll();

		if (!this.rolling)
		{
			if (Goblet.getGoblet().getInput().isInputDown("left"))
			{
				this.motion.x = -this.speed;
			}

			if (Goblet.getGoblet().getInput().isInputDown("right"))
			{
				this.motion.x = this.speed;
			}

			if (Goblet.getGoblet().getInput().isInputDown("up"))
			{
				this.motion.y = this.speed;
			}

			if (Goblet.getGoblet().getInput().isInputDown("down"))
			{
				this.motion.y = -this.speed;
			}

			if (Goblet.getGoblet().getInput().isInputReleased("mouseright"))
			{
				this.direction.normalize();
				Transform3 transform = this.transform.derive3();
				transform.position.add(this.direction.x(), this.direction.y(), 0);

				this.world.spawnEntity(this.world.getRandom().nextBoolean() ? new EntitySlash(this.world, transform) : new EntityThrust(this.world, transform));
			}

			if (Goblet.getGoblet().getInput().isInputReleased("mouseleft"))
			{
				float dist = this.direction.length();
				float throwSpeed = MathUtil.clamp(dist * 0.025F, 0, 0.25F);
				float zSpeed = (dist * EntityThrowable.GRAVITY / 2F) / throwSpeed;
				this.direction.normalize().mul(throwSpeed);

				this.world.spawnEntity(new EntityBomb(this.world, this.world.createTransform(this.transform), this.direction.x(), this.direction.y(), zSpeed, Explosions.getRandomExplosion(this.world.getRandom())));
			}
		}
	}

	@Override
	public void onMotionUpdate()
	{
		if (!this.rolling)
		{
			super.onMotionUpdate();
		}
	}

	@Override
	public void onPreCollisionUpdate()
	{
	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		this.move(collision.getDelta().x(), collision.getDelta().y());

		return false;
	}

	@Override
	public void onPostCollisionUpdate()
	{

	}

	@Override
	public boolean canCollideWith(BoxCollider collider)
	{
		return collider instanceof Room;
	}
}
