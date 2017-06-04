package net.jimboi.glim;

import net.jimboi.base.Main;

import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 6/1/17.
 */
public abstract class SceneGlimBase extends Scene
{
	protected RendererGlim renderer;

	protected EntityManager entityManager;

	public SceneGlimBase()
	{
	}

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
	}

	@Override
	protected final void onSceneLoad()
	{
		this.renderer = new RendererGlim();
		Main.RENDERENGINE.add(this.renderer);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.entityManager.update();
	}

	@Override
	protected final void onSceneUnload()
	{
		Main.RENDERENGINE.remove(this.renderer);
		this.renderer = null;
	}

	@Override
	protected void onSceneDestroy()
	{
		this.entityManager.clear();
	}

	public final EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public final RendererGlim getRenderer()
	{
		return this.renderer;
	}
}
