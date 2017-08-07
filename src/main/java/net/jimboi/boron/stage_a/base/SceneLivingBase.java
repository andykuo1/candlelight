package net.jimboi.boron.stage_a.base;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentTransform;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;

import org.bstone.living.LivingManager;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class SceneLivingBase<T extends LivingEntity> extends SceneEntityBase implements LivingManager.OnLivingCreateListener<T>, LivingManager.OnLivingDestroyListener<T>
{
	protected LivingManager<T> livingManager;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.livingManager = new LivingManager<>();
		this.livingManager.onLivingCreate.addListener(this);
		this.livingManager.onLivingDestroy.addListener(this);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		super.onSceneUpdate(delta);

		this.livingManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.livingManager.destroy();

		super.onSceneStop();
	}

	public <R extends T> R spawn(R living)
	{
		this.livingManager.add(living);
		return living;
	}

	protected void onLivingEntityCreate(T living, Entity entity)
	{
		entity.addComponent(new EntityComponentTransform(living.getTransform()));
		if (living.getRenderable() != null)
		{
			entity.addComponent(living.getRenderable());
		}
	}

	protected void onLivingEntityDestroy(T living, Entity entity)
	{

	}

	@Override
	public final void onLivingCreate(T living)
	{
		Entity entity = this.getEntityManager().createEntity(living);
		this.onLivingEntityCreate(living, entity);
		living.onEntityCreate(entity);
	}

	@Override
	public final void onLivingDestroy(T living)
	{
		Entity entity = this.getEntityManager().getEntityByComponent(living);
		entity.setDead();
		this.onLivingEntityDestroy(living, entity);
		living.onEntityDestroy(entity);
	}

	public LivingManager<T> getLivingManager()
	{
		return this.livingManager;
	}
}