package canary.cuplet.scene_main.entity;

import canary.base.collisionbox.collider.BoxCollider;
import canary.base.collisionbox.response.CollisionResponse;
import canary.cuplet.Cuplet;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.MainRenderer;
import canary.cuplet.scene_main.component.ComponentDamageable;
import canary.cuplet.scene_main.component.ComponentMotion;
import canary.cuplet.scene_main.tick.TickCounter;
import canary.cuplet.scene_main.tile.TileMap;

import canary.bstone.input.InputEngine;
import canary.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import canary.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityPlayer extends EntityHurtable
{
	private Vector2f direction = new Vector2f();
	private Vector2f mousePos = new Vector2f();

	private float speed;

	private float rollSpeed = 0.25F;
	private boolean rolling;

	protected final TickCounter rollingTicks = new TickCounter(6);
	protected final TickCounter rollingInputTicks = new TickCounter(12);
	private int maxRollingCooldownTicks = 15;

	private int rollingInputDir = -1;

	public EntityPlayer(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.6F),
				world.createRenderable2D(transform, '@', 0xFF00FF));

		this.speed = 0.03F;
	}

	protected void onEntitySetup()
	{
		super.onEntitySetup();

		ComponentMotion componentMotion = this.addComponent(new ComponentMotion());
		componentMotion.setFriction(0.3F);

		ComponentDamageable componentDamageable  = this.getComponent(ComponentDamageable.class);
		componentDamageable.canTakeDamageFrom = (damageSource) -> !this.rolling;
		componentDamageable.setMaxHealth(100);
	}

	@Override
	protected void onEntityDelete()
	{
		super.onEntityDelete();
	}

	@Override
	public boolean canSetFire(float x, float y, float strength)
	{
		if (this.rolling) return false;
		return super.canSetFire(x, y, strength);
	}

	private void roll(float dx, float dy)
	{
		this.rolling = true;
		this.rollingTicks.reset();

		final ComponentMotion componentMotion = this.getComponent(ComponentMotion.class);
		componentMotion.addMotion(dx * this.rollSpeed, dy * this.rollSpeed);
		componentMotion.setOnGround(false);
	}

	private void updateRollInput(float dx, float dy, int inputDir)
	{
		if (this.rollingInputTicks.getTicks() == -1 && this.rollingInputDir != inputDir)
		{
			this.rollingInputTicks.reset();
			this.rollingInputDir = inputDir;
		}
		else if (this.rollingInputDir == inputDir && !this.rollingInputTicks.isBuffering() && !this.rollingInputTicks.isComplete())
		{
			this.roll(dx, dy);
		}
	}

	private void updateRoll()
	{
		if (this.rolling)
		{
			//Control the roll
			this.rollingTicks.tick();
			if (this.rollingTicks.isComplete())
			{
				this.rolling = false;
				this.rollingInputTicks.resetWithBuffer(this.maxRollingCooldownTicks);
				this.rollingInputDir = -1;
				this.getComponent(ComponentMotion.class).setOnGround(true);
			}
		}
		else
		{
			//Try to start a roll
			if (this.rollingInputTicks.isComplete())
			{
				this.rollingInputTicks.resetWithBuffer(1);
				this.rollingInputDir = -1;
			}
			else if (!this.rollingInputTicks.isBuffering() || this.rollingInputTicks.getTicks() < -1)
			{
				this.rollingInputTicks.tick();
			}
		}
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		InputEngine input = Cuplet.getCuplet().getInputEngine();

		if (!this.rolling)
		{
			if (input.getAction("rollLeft"))
			{
				this.updateRollInput(-1, 0, 0);
			}
			else if (input.getAction("rollRight"))
			{
				this.updateRollInput(1, 0, 1);
			}
			else if (input.getAction("rollUp"))
			{
				this.updateRollInput(0, 1, 2);
			}
			else if (input.getAction("rollDown"))
			{
				this.updateRollInput(0, -1, 3);
			}
		}

		float mouseX = input.getRange("mousex");
		float mouseY = input.getRange("mousey");
		Vector2fc mouse = ((MainRenderer) Cuplet.getCuplet().getSceneManager().getCurrentRenderer()).getScreenSpace().getPoint2DFromScreen(mouseX, mouseY, this.mousePos);
		mouse.sub(this.transform.posX(), this.transform.posY(), this.direction);
		this.transform.rotation.rotationZ((float) Math.atan2(this.direction.y(), this.direction.x()));

		this.updateRoll();

		if (!this.rolling)
		{
			final ComponentMotion componentMotion = this.getComponent(ComponentMotion.class);
			if (input.getState("left"))
			{
				componentMotion.addMotion(-this.speed, 0);
			}
			if (input.getState("right"))
			{
				componentMotion.addMotion(this.speed, 0);
			}
			if (input.getState("up"))
			{
				componentMotion.addMotion(0, this.speed);
			}
			if (input.getState("down"))
			{
				componentMotion.addMotion(0, -this.speed);
			}

			if (input.getAction("fireright"))
			{
				this.direction.normalize().mul(0.8F);
				Transform3 transform = this.transform.derive3();
				transform.position.add(this.direction.x(), this.direction.y(), 0);

				this.world.spawnEntity(this.world.getRandom().nextBoolean() ? new EntitySlash(this.world, transform, this) : new EntityThrust(this.world, transform, this));
			}

			if (input.getAction("fireleft"))
			{
				float dist = this.direction.length();
				if (dist < 1F)
				{
					this.direction.mul(0.05F);
					this.world.spawnEntity(new EntityGrenade(this.world, this.world.createTransform(this.transform), this.direction.x(), this.direction.y(), 0, Explosions.getRandomExplosion(this.world.getRandom())));
				}
				else
				{
					float throwSpeed = MathUtil.clamp(dist * 0.025F, 0, 0.25F);
					float zSpeed = (dist * EntityThrowable.GRAVITY / 2F) / throwSpeed;
					this.direction.normalize().mul(throwSpeed);

					this.world.spawnEntity(new EntityGrenade(this.world, this.world.createTransform(this.transform), this.direction.x(), this.direction.y(), zSpeed, Explosions.getRandomExplosion(this.world.getRandom())));
				}
			}

			if (input.getAction("action"))
			{
				System.out.println("ACTION!");
			}
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
		return collider instanceof TileMap;
	}

	@Override
	public boolean isColliderActive()
	{
		return true;
	}
}
