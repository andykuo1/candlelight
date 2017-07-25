package net.jimboi.boron.base;

import org.bstone.material.MaterialManager;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;
import org.qsilver.scene.Scene;
import org.zilar.animation.AnimationManager;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class SceneBase extends Scene
{
	private RenderService renderer;

	protected EntityManager entityManager;
	protected MaterialManager materialManager;
	protected AnimationManager animationManager;

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
		this.materialManager = new MaterialManager();
		this.animationManager = new AnimationManager();
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderEngine)
	{
		try
		{
			this.renderer = this.getRenderClass().newInstance();

			if (this.renderer instanceof RenderBase)
			{
				((RenderBase) this.renderer).scene = this;
			}
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid render service class! Must have defined default constructor!");
		}

		renderEngine.startService(this.renderer);
	}

	@Override
	protected abstract void onSceneStart();

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.animationManager.update(delta);
		this.entityManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderEngine)
	{
		renderEngine.stopService(this.renderer);
	}

	@Override
	protected void onSceneDestroy()
	{
		this.materialManager.clear();
	}

	protected abstract Class<? extends RenderService> getRenderClass();

	protected RenderService getRender()
	{
		return this.renderer;
	}

	public AnimationManager getAnimationManager()
	{
		return this.animationManager;
	}

	public MaterialManager getMaterialManager()
	{
		return this.materialManager;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}