package boron.stage_a.goblet.entity;

import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.base.collisionbox.response.CollisionResponse;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentMotion;
import boron.stage_a.goblet.entity.base.EntitySolid;
import boron.stage_a.goblet.tick.TickCounter;
import boron.stage_a.goblet.tile.TileMap;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/13/17.
 */
public class EntityArrow extends EntitySolid
{
	protected final TickCounter ageTicks = new TickCounter(200);
	float dx;
	float dy;

	public EntityArrow(GobletWorld world, Transform3 transform, float dx, float dy)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.1F),
				world.createRenderable2D(transform, '!', 0xFFFF00));

		this.getRenderable().getRenderModel().transformation().rotationZ((float) Math.atan2(dy, dx) + Transform3.HALF_PI).translate(0.25F, 0, 0);

		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		ComponentMotion componentMotion = this.addComponent(new ComponentMotion());
		componentMotion.setMotion(this.dx, this.dy);
		componentMotion.setOnGround(false);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		this.ageTicks.tick();
		if (this.ageTicks.isComplete())
		{
			this.setDead();
		}
	}

	@Override
	public void onLivingLateUpdate()
	{
		super.onLivingLateUpdate();

		ComponentMotion.onLivingLateUpdate(this);
	}

	@Override
	public void onPreCollisionUpdate()
	{

	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		ComponentMotion.move(this, collision.getDelta().x(), collision.getDelta().y());
		ComponentMotion componentMotion = this.getComponent(ComponentMotion.class);
		componentMotion.setMotion(0, 0);

		return true;
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
	public boolean isSolid()
	{
		return false;
	}

	@Override
	public boolean isColliderActive()
	{
		return true;
	}
}