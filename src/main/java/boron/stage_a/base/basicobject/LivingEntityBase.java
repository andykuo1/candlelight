package boron.stage_a.base.basicobject;

import boron.bstone.entity.EntityManager;
import boron.bstone.living.LivingManager;
import boron.bstone.livingentity.LivingEntity;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/12/17.
 */
public class LivingEntityBase extends LivingEntity
{
	protected final Transform3 transform;
	protected final ComponentRenderable renderable;

	public LivingEntityBase(Transform3 transform, ComponentRenderable renderable)
	{
		this.transform = transform;
		this.renderable = renderable;
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		this.addComponent(new ComponentTransform3(this.transform));
		this.addComponent(this.renderable);
	}

	@Override
	public void onLivingCreate(LivingManager livingManager)
	{

	}

	@Override
	public void onLivingUpdate()
	{

	}

	@Override
	public void onLivingLateUpdate()
	{

	}

	@Override
	public void onLivingDestroy()
	{

	}

	@Override
	public void onEntityDestroy()
	{

	}

	public final ComponentRenderable getRenderable()
	{
		return this.renderable;
	}

	public final Transform3 getTransform()
	{
		return this.transform;
	}
}
