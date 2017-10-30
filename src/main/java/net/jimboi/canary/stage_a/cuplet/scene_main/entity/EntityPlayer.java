package net.jimboi.canary.stage_a.cuplet.scene_main.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.canary.stage_a.cuplet.Cuplet;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentDamageable;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentMotion;
import net.jimboi.canary.stage_a.cuplet.scene_main.tick.TickCounter;
import net.jimboi.canary.stage_a.cuplet.scene_main.tile.TileMap;

import org.bstone.entity.EntityManager;
import org.bstone.input.context.InputContext;
import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

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

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		ComponentMotion componentMotion = this.addComponent(new ComponentMotion());
		componentMotion.setFriction(0.3F);

		ComponentDamageable componentDamageable  = this.getComponent(ComponentDamageable.class);
		componentDamageable.canTakeDamageFrom = (damageSource) -> !this.rolling;
		componentDamageable.setMaxHealth(100);
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

			InputContext ctx = Cuplet.getCuplet().getFramework().getInputEngine().getDefaultContext();
			if (ctx.getState("left").isPressed())
			{
				this.updateRollInput(-1, 0, 0);
			}

			if (ctx.getState("right").isPressed())
			{
				this.updateRollInput(1, 0, 1);
			}

			if (ctx.getState("up").isPressed())
			{
				this.updateRollInput(0, 1, 2);
			}

			if (ctx.getState("down").isPressed())
			{
				this.updateRollInput(0, -1, 3);
			}
		}
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		InputContext ctx = Cuplet.getCuplet().getFramework().getInputEngine().getDefaultContext();
		float mousex = ctx.getRange("mousex").getRange();
		float mousey = ctx.getRange("mousey").getRange();
		Vector2fc mouse = Cuplet.getCuplet().getRender().getScreenSpace().getPoint2DFromScreen(mousex, mousey, this.mousePos);
		mouse.sub(this.transform.posX(), this.transform.posY(), this.direction);
		this.transform.rotation.rotationZ((float) Math.atan2(this.direction.y(), this.direction.x()));

		this.updateRoll();

		if (!this.rolling)
		{
			final ComponentMotion componentMotion = this.getComponent(ComponentMotion.class);
			if (ctx.getState("left").isDown())
			{
				componentMotion.addMotion(-this.speed, 0);
			}

			if (ctx.getState("right").isDown())
			{
				componentMotion.addMotion(this.speed, 0);
			}

			if (ctx.getState("up").isDown())
			{
				componentMotion.addMotion(0, this.speed);
			}

			if (ctx.getState("down").isDown())
			{
				componentMotion.addMotion(0, -this.speed);
			}

			if (ctx.getState("mouseright").isReleased())
			{
				this.direction.normalize().mul(0.8F);
				Transform3 transform = this.transform.derive3();
				transform.position.add(this.direction.x(), this.direction.y(), 0);

				this.world.spawnEntity(this.world.getRandom().nextBoolean() ? new EntitySlash(this.world, transform, this) : new EntityThrust(this.world, transform, this));
			}

			if (ctx.getState("mouseleft").isReleased())
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

			if (ctx.getState("action").isReleased())
			{
				EntityVillager villager = this.world.getNearestEntity(this.transform.posX(), this.transform.posY(), EntityVillager.class);
				if (villager != null)
				{
					if (villager.getTransform().position.distanceSquared(this.transform.position3()) < 1F)
					{
						villager.onInteract(this);
					}
				}
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
