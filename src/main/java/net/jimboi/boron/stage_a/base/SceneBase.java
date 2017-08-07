package net.jimboi.boron.stage_a.base;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.qsilver.scene.Scene;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class SceneBase extends Scene
{
	private RenderService render;

	@Override
	protected void onSceneLoad(RenderEngine renderEngine)
	{
		try
		{
			this.render = this.getRenderClass().getConstructor(RenderEngine.class).newInstance(renderEngine);

			if (this.render instanceof RenderSceneBase)
			{
				((RenderSceneBase) this.render).scene = this;
			}
		}
		catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
		{
			throw new IllegalArgumentException("Invalid render service class! Must have defined a constructor with RenderEngine as the only argument!");
		}

		this.render.start();
	}

	@Override
	protected void onSceneUnload(RenderEngine renderEngine)
	{
		this.render.stop();
	}

	protected abstract Class<? extends RenderService> getRenderClass();

	public RenderService getRender()
	{
		return this.render;
	}
}
