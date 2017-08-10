package net.jimboi.boron.stage_a.base.livingentity;

import org.bstone.living.LivingManager;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 8/5/17.
 */
public class LivingEntityManager<L extends LivingEntity>
{
	private EntityManager entityManager;
	private LivingManager<L> livingManager;
	private LivingManagerListener listener;

	public LivingEntityManager()
	{
		this.entityManager = new EntityManager();
		this.livingManager = new LivingManager<>();
		this.listener = new LivingManagerListener();
		this.livingManager.onLivingCreate.addListener(this.listener);
		this.livingManager.onLivingDestroy.addListener(this.listener);
	}

	public void update()
	{
		this.livingManager.update();
		this.entityManager.update();
	}

	public void destroy()
	{
		this.livingManager.onLivingCreate.deleteListener(this.listener);
		this.livingManager.onLivingDestroy.deleteListener(this.listener);
	}

	public <R extends L> R spawn(R living)
	{
		this.livingManager.add(living);
		return living;
	}

	protected void onLivingEntityCreate(L living, Entity entity)
	{
		entity.addComponent(new EntityComponentTransform(living.getTransform()));
		if (living.getRenderable() != null)
		{
			entity.addComponent(living.getRenderable());
		}
	}

	protected void onLivingEntityDestroy(L living, Entity entity)
	{
	}

	public LivingManager<L> getLivings()
	{
		return this.livingManager;
	}

	public EntityManager getEntities()
	{
		return this.entityManager;
	}

	private class LivingManagerListener implements LivingManager.OnLivingCreateListener<L>, LivingManager.OnLivingDestroyListener<L>
	{
		@Override
		public void onLivingCreate(L living)
		{
			Entity entity = LivingEntityManager.this.entityManager.createEntity(living);
			LivingEntityManager.this.onLivingEntityCreate(living, entity);
			living.onEntityCreate(entity);
		}

		@Override
		public void onLivingDestroy(L living)
		{
			Entity entity = LivingEntityManager.this.entityManager.getEntityByComponent(living);
			entity.setDead();
			LivingEntityManager.this.onLivingEntityDestroy(living, entity);
			living.onEntityDestroy(entity);
		}
	}
}
