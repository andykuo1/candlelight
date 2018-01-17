package org.bstone.render;

import org.bstone.kernel.Engine;
import org.bstone.window.Window;

/**
 * An engine that handles the rendering of the application
 */
public class RenderEngine implements Engine
{
	private final Window window;

	private final RenderFramework renderable;

	private double timeStep;
	private double timeDelta;
	private double timePrevious;

	public RenderEngine(Window window, RenderFramework renderable)
	{
		this.window = window;

		this.renderable = renderable;
	}

	@Override
	public boolean initialize()
	{
		this.timeStep = 1000000000D / this.window.getRefreshRate();
		this.timePrevious = System.nanoTime();

		this.renderable.load();

		return true;
	}

	@Override
	public void update()
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;
		this.timePrevious = current;
		this.timeDelta = elapsed / this.timeStep;

		this.window.clearScreenBuffer();
		{
			this.renderable.render();

			--this.timeDelta;
		}
		this.window.updateScreenBuffer();

		this.window.poll();
	}

	@Override
	public void terminate()
	{
		this.renderable.unload();
	}

	public double getElapsedFrameTime()
	{
		return this.timeDelta;
	}

	public final Window getWindow()
	{
		return this.window;
	}

	public final RenderFramework getRenderable()
	{
		return this.renderable;
	}
}
