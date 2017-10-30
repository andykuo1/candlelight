package org.bstone.render;

import org.bstone.application.Application;
import org.bstone.application.Engine;
import org.bstone.application.handler.FrameHandler;
import org.bstone.application.handler.RenderHandler;
import org.bstone.service.ServiceManager;
import org.bstone.tick.TickCounter;
import org.bstone.window.Window;

/**
 * Created by Andy on 10/12/17.
 */
public class RenderEngine extends Engine
{
	private final Window window;

	private final ServiceManager<RenderService, RenderEngine> serviceManager;

	private final TickCounter frameCounter;
	private final FrameHandler frameHandler;
	private final RenderHandler renderHandler;

	public RenderEngine(Window window, FrameHandler frameHandler, RenderHandler renderHandler)
	{
		this.window = window;
		this.serviceManager = new ServiceManager<>(this);

		this.frameHandler = frameHandler;
		this.renderHandler = renderHandler;

		this.frameCounter = new TickCounter();
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.frameCounter.reset();

		this.renderHandler.onRenderLoad();

		this.serviceManager.beginServiceBlock();
		this.serviceManager.endServiceBlock();

		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		if (this.frameHandler.shouldRenderCurrentFrame())
		{
			this.window.clearScreenBuffer();
			{
				double delta = this.frameHandler.getElapsedFrameTime();

				this.serviceManager.beginServiceBlock();
				{
					this.renderHandler.onRenderUpdate(delta);
					this.serviceManager.forEach(service->service.onRenderUpdate(this, delta));
				}
				this.serviceManager.endServiceBlock();
			}
			this.window.updateScreenBuffer();

			this.frameHandler.onFrameRendered();

			this.frameCounter.tick();
		}

		this.window.poll();

		if (this.window.shouldCloseWindow())
		{
			app.stop();
		}
	}

	@Override
	protected void onStop(Application app)
	{
		this.serviceManager.beginServiceBlock();
		this.serviceManager.endServiceBlock();

		this.renderHandler.onRenderUnload();
		this.serviceManager.clearServices();
	}

	public final TickCounter getFrameCounter()
	{
		return this.frameCounter;
	}

	public final ServiceManager<RenderService, RenderEngine> getRenderServices()
	{
		return this.serviceManager;
	}
}
