package org.bstone.render;

import org.bstone.kernel.Engine;
import org.bstone.window.Window;

/**
 * An engine that handles the rendering of the application
 */
public class RenderEngine implements Engine
{
	private final Window window;

	private Renderable renderable;

	private final boolean limitFrameRate;
	private final double timeStep;
	private double timeDelta;
	private double timePrevious;
	private boolean dirty = true;

	public RenderEngine(Window window, int framesPerSecond, boolean limitFrameRate)
	{
		this.window = window;

		this.limitFrameRate = limitFrameRate;
		this.timeStep = 1000000000D / framesPerSecond;
	}

	public RenderEngine setRenderable(Renderable renderable)
	{
		this.renderable = renderable;
		return this;
	}

	@Override
	public boolean initialize()
	{
		this.timePrevious = System.nanoTime();

		this.renderable.load();

		return true;
	}

	@Override
	public void update()
	{
		if (this.dirty || !this.limitFrameRate)
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

			this.dirty = false;
		}

		this.window.poll();
	}

	@Override
	public void terminate()
	{
		this.renderable.unload();
	}

	public void markDirty()
	{
		this.dirty = true;
	}

	public double getElapsedFrameTime()
	{
		return this.timeDelta;
	}

	public final Window getWindow()
	{
		return this.window;
	}

	public final Renderable getRenderable()
	{
		return this.renderable;
	}
}
