package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.MotionHelper;
import net.jimboi.boron.stage_a.smack.Smack;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.tile.DungeonHandler;

import org.bstone.livingentity.LivingEntity;
import org.bstone.transform.Transform3;
import org.bstone.window.input.InputManager;
import org.joml.Vector2f;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/5/17.
 */
public class EntityPlayer extends EntityMotion implements ActiveBoxCollider
{
	private int ammo = 7;

	private int maxCooldown = 5;
	private int cooldown;

	private float bulletSpeed = 0.2F;

	private Vector2f lookDir = new Vector2f(-1, 0);

	private Vector2f mouseTarget = new Vector2f();
	private Vector2f moveTarget = new Vector2f();
	private float speed = 0.06F;

	private float pickupRadius = 2;

	public EntityPlayer(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 0.4F, world.createRenderable2D(transform, '@', 0xFF00FF));

		this.getRenderable().getRenderModel().transformation().scale(0.8F, 0.8F, 1);

		this.moveTarget.set(transform.position3().x(), transform.position3().y());
		this.friction = 0.1F;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		final InputManager input = Smack.getSmack().getInput();
		final float mouseX = input.getInputAmount("mousex");
		final float mouseY = input.getInputAmount("mousey");
		Smack.getSmack().getScreenSpace().getPoint2DFromScreen(mouseX, mouseY, this.mouseTarget);

		if (input.isInputDown("mouseleft"))
		{
			this.moveTarget.set(this.mouseTarget);
		}

		if (input.isInputReleased("mouseleft"))
		{
			this.world.spawn(new EntityPing(this.world, new Transform3().setPosition(this.moveTarget.x(), this.moveTarget.y(), 0), 0xFF0000));
		}

		if (input.isInputDown("mouseright"))
		{
			MotionHelper.getDirectionTowards(this.transform, this.mouseTarget.x(), this.mouseTarget.y(), this.lookDir);

			this.transform.rotation.rotationZ(MotionHelper.getRadiansFromDirection(this.lookDir.x(), this.lookDir.y()));
		}

		if (input.isInputReleased("mouseright"))
		{
			if (this.cooldown == 0 && this.ammo > 0)
			{
				this.fireBullet();
				--this.ammo;
				this.cooldown = this.maxCooldown;
			}
		}

		if (this.cooldown != 0)
		{
			--this.cooldown;
		}

		if (!MotionHelper.isWithinDistanceSquared(this.transform, this.moveTarget.x(), this.moveTarget.y(), 0.01F))
		{
			MotionHelper.getDirectionTowards(this.transform, this.moveTarget.x(), this.moveTarget.y(), this.motion);
			this.motion.mul(this.speed);
		}
		else
		{
			this.motion.set(0);
		}

		for(LivingEntity living : this.world.getSmacks().getLivingEntities())
		{
			if (living instanceof EntityAmmo)
			{
				EntityAmmo entityAmmo = (EntityAmmo) living;
				if (MotionHelper.isWithinDistanceSquared(this.transform, entityAmmo.getTransform().position3().x(), entityAmmo.getTransform().position3().y(), this.pickupRadius * this.pickupRadius))
				{
					((EntityAmmo) living).setPosition(MathUtil.lerp(entityAmmo.getTransform().position3().x(), this.transform.position3().x(), 0.06F), MathUtil.lerp(entityAmmo.getTransform().position3().y(), this.transform.position3().y(), 0.06F));
				}
			}
		}
	}

	public void fireBullet()
	{
		Transform3 transform = new Transform3();
		transform.position.set(this.transform.position3());
		transform.rotation.rotationZ(this.transform.eulerRadians().z());

		this.world.spawn(new EntityBullet(this.world, transform, 0xFFFF00, this.bulletSpeed));
	}

	public int getAmmo()
	{
		return this.ammo;
	}

	@Override
	public void onPreCollisionUpdate()
	{
		this.boundingBox.setCenter(this.transform.position3().x(), this.transform.position3().y());
	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		BoxCollider other = collision.getCollider();
		if (other instanceof EntityBoulder)
		{
			this.move(collision.getDelta().x(), collision.getDelta().y());
		}
		else if (other instanceof EntityAmmo)
		{
			this.ammo += ((EntityAmmo) other).getAmount();

			((EntityAmmo) other).damage(new DamageSource(this), 1);
		}
		else if (other instanceof DungeonHandler)
		{
			this.move(collision.getDelta().x(), collision.getDelta().y());
		}
		return false;
	}

	@Override
	public void onPostCollisionUpdate()
	{
	}

	@Override
	public boolean canCollideWith(BoxCollider collider)
	{
		return (collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityAmmo || collider instanceof EntityBoulder)) || collider instanceof DungeonHandler;
	}
}
