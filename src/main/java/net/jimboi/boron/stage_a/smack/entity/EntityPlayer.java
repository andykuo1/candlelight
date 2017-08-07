package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.MotionHelper;
import net.jimboi.boron.stage_a.smack.Smack;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxActiveCollider;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxCollider;
import net.jimboi.boron.stage_a.smack.aabb.IntersectionData;

import org.bstone.transform.Transform3;
import org.bstone.window.input.InputManager;
import org.joml.Vector2f;
import org.qsilver.util.MathUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 8/5/17.
 */
public class EntityPlayer extends SmackEntity implements BoundingBoxActiveCollider
{
	private int ammo = 7;

	private int maxCooldown = 5;
	private int cooldown;

	private float bulletSpeed = 0.4F;

	private Vector2f lookDir = new Vector2f(-1, 0);

	private Vector2f mouseTarget = new Vector2f();
	private Vector2f moveTarget = new Vector2f();
	private Vector2f velocity = new Vector2f();
	private float speed = 0.1F;

	private float pickupRadius = 2;

	public EntityPlayer(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 0.8F, world.createRenderable2D(transform, '@', 0xFF00FF));
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

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
			MotionHelper.getDirectionTowards(this.transform, this.moveTarget.x(), this.moveTarget.y(), this.velocity);
			this.velocity.mul(this.speed);

			this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
		}

		Iterator<LivingEntity> livings = this.world.getSmacks().getLivingManager().getLivingIterator();
		while (livings.hasNext())
		{
			LivingEntity living = livings.next();
			if (living instanceof EntityAmmo)
			{
				if (MotionHelper.isWithinDistanceSquared(this.transform, living.getTransform().position3().x(), living.getTransform().position3().y(), this.pickupRadius * this.pickupRadius))
				{
					((EntityAmmo) living).setPosition(MathUtil.lerp(living.getTransform().position3().x(), this.transform.position3().x(), 0.06F), MathUtil.lerp(living.getTransform().position3().y(), this.transform.position3().y(), 0.06F));
				}
			}
		}
	}

	@Override
	public void onCheckIntersection(List<IntersectionData> intersections)
	{
		if (!intersections.isEmpty())
		{
			SmackEntity other = (SmackEntity) intersections.get(0).getCollider();

			this.velocity.set(0);
			for(IntersectionData intersection : intersections)
			{
				BoundingBoxCollider collider = intersection.getCollider();
				if (collider instanceof EntityBoulder)
				{
					this.velocity.sub(intersection.getDelta().x(), intersection.getDelta().y());
				}
				else if (collider instanceof EntityAmmo)
				{
					this.ammo += ((EntityAmmo) collider).getAmount();

					other.damage(new DamageSource(this), 1);
				}
			}
			this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
			this.velocity.set(0);
		}
	}

	@Override
	public boolean canCollideWith(BoundingBoxCollider collider)
	{
		return collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityAmmo || collider instanceof EntityBoulder);
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
}
