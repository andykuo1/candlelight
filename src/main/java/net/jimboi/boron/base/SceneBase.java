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
public abstract class SceneBase<R extends RenderService> extends Scene
{
	private R renderer;

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
				RenderBase render = (RenderBase) this.renderer;
				if (!render.getSceneClass().equals(this.getClass()))
				{
					throw new InstantiationException();
				}
				render.scene = this;
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

	protected abstract Class<R> getRenderClass();

	protected final R getRender()
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