package org.bstone.render;

import org.bstone.service.ServiceManager;

/**
 * Created by Andy on 8/4/17.
 */
public class RenderEngine
{
	private final ServiceManager<RenderService, RenderEngine> serviceManager;

	private final RenderHandler handler;

	public RenderEngine(RenderHandler handler)
	{
		this.handler = handler;

		this.serviceManager = new ServiceManager<>(this);
	}

	public void load()
	{
		this.handler.onRenderLoad(this);
	}

	public void unload()
	{
		this.handler.onRenderUnload(this);
		this.serviceManager.clearServices();
	}

	public void update(double delta)
	{
		this.serviceManager.beginServiceBlock();
		{
			this.handler.onRenderUpdate(this, delta);
			this.serviceManager.forEach(service->service.onRenderUpdate(this, delta));
		}
		this.serviceManager.endServiceBlock();
	}

	public final ServiceManager<RenderService, org.bstone.render.RenderEngine> getRenderServices()
	{
		return this.serviceManager;
	}
}
