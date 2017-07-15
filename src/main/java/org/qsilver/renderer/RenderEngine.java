package org.qsilver.renderer;

import org.bstone.util.listener.Listenable;
import org.qsilver.service.ServiceManager;

/**
 * Created by Andy on 5/30/17.
 */
public class RenderEngine
{
	public interface OnRenderUpdateListener
	{
		void onRenderUpdate(RenderEngine renderEngine);
	}

	public final Listenable<OnRenderUpdateListener> onRenderUpdate = new Listenable<>((listener, objects) -> listener.onRenderUpdate((RenderEngine) objects[0]));

	ServiceManager<RenderEngine> serviceManager;

	public RenderEngine()
	{
		this.serviceManager = new ServiceManager<>(this);
	}

	public void start()
	{
		this.serviceManager = new ServiceManager<>(this);
	}

	public void stop()
	{
		if (!this.getServiceManager().isEmpty())
		{
			this.serviceManager.clear();
			this.serviceManager = null;
		}
	}

	public void update()
	{
		this.serviceManager.beginServiceBlock();

		this.onRenderUpdate.notifyListeners(this);

		this.serviceManager.endServiceBlock();
	}

	public ServiceManager<RenderEngine> getServiceManager()
	{
		return this.serviceManager;
	}
}
