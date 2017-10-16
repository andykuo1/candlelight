package org.bstone.render;

import org.bstone.application.Application;
import org.bstone.application.Engine;
import org.bstone.service.ServiceManager;
import org.bstone.tick.TickCounter;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;

/**
 * Created by Andy on 10/12/17.
 */
public class RenderEngine extends Engine
{
	private Window window;
	private TickEngine tickEngine;

	private TickCounter frameCounter;
	private RenderHandler renderHandler;
	private final ServiceManager<RenderService, RenderEngine> serviceManager;

	private double timeCounter;

	public RenderEngine(RenderHandler renderHandler)
	{
		this.renderHandler = renderHandler;
		this.serviceManager = new ServiceManager<>(this);

		this.frameCounter = new TickCounter();
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.window = app.getWindow();
		this.tickEngine = app.getEngineByClass(TickEngine.class);

		this.frameCounter.reset();
		this.timeCounter = System.currentTimeMillis();

		this.renderHandler.onRenderLoad();
		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		if (this.tickEngine.shouldRenderFrame())
		{
			this.window.updateScreenBuffer();
			{
				double delta = this.tickEngine.getElapsedFrameTime();

				this.serviceManager.beginServiceBlock();
				{
					this.renderHandler.onRenderUpdate(delta);
					this.serviceManager.forEach(service->service.onRenderUpdate(this, delta));
				}
				this.serviceManager.endServiceBlock();
			}
			this.window.clearScreenBuffer();

			this.tickEngine.setFrameUpdated();
			this.frameCounter.tick();
		}

		this.window.poll();

		if (this.window.shouldCloseWindow())
		{
			app.stop();
		}

		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;

			System.out.println("[UPS: " + this.tickEngine.getUpdateCounter().get() + " || FPS: " + this.frameCounter.get() + "]");
		}
	}

	@Override
	protected void onStop(Application app)
	{
		this.renderHandler.onRenderUnload();
		this.serviceManager.clearServices();
	}

	public TickCounter getFrameCounter()
	{
		return this.frameCounter;
	}

	public final ServiceManager<RenderService, RenderEngine> getRenderServices()
	{
		return this.serviceManager;
	}
}
