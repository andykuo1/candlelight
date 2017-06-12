package net.jimboi.stage_b.glim;

import net.jimboi.base.Main;
import net.jimboi.stage_b.glim.gameentity.GameEntityManager;

import org.qsilver.scene.Scene;

/**
 * Created by Andy on 6/1/17.
 */
public abstract class SceneGlimBase extends Scene
{
	protected RendererGlim renderer;

	protected GameEntityManager entityManager;

	public SceneGlimBase()
	{
	}

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new GameEntityManager();
	}

	@Override
	protected final void onSceneLoad()
	{
		this.renderer = new RendererGlim((SceneGlim) this);
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

	public final GameEntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public final RendererGlim getRenderer()
	{
		return this.renderer;
	}
}
