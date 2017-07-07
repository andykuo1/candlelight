package org.qsilver.renderer;

import org.bstone.util.listener.Listenable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 5/30/17.
 */
public class RenderEngine
{
	public interface OnRenderUpdateListener
	{
		void onRenderUpdate();
	}

	public final Listenable<OnRenderUpdateListener> onRenderUpdate = new Listenable<>((listener, objects) -> listener.onRenderUpdate());

	private final Set<RenderManager> createSet = new HashSet<>();
	private final Set<RenderManager> destroySet = new HashSet<>();
	private final Set<RenderManager> renderers = new HashSet<>();

	private final Set<RenderManager> cachedSet = new HashSet<>();
	private boolean cacheCreate = false;
	private boolean cacheDestroy = false;

	public void add(RenderManager renderer)
	{
		if (this.cacheCreate)
		{
			this.cachedSet.add(renderer);
		}
		else
		{
			this.createSet.add(renderer);
		}
	}

	public void remove(RenderManager renderer)
	{
		if (this.cacheDestroy)
		{
			this.cachedSet.add(renderer);
		}
		else
		{
			this.destroySet.add(renderer);
		}
	}

	public void start()
	{
		this.flush();
	}

	public void stop()
	{
		this.destroy();
	}

	public void update()
	{
		this.flush();

		for (RenderManager renderer : this.renderers)
		{
			renderer.onRenderUpdate();
			renderer.onRenderUpdate.notifyListeners();
		}

		this.onRenderUpdate.notifyListeners();
	}

	public void flush()
	{
		while (!this.createSet.isEmpty())
		{
			this.cacheCreate = true;
			for (RenderManager renderer : this.createSet)
			{
				renderer.onRenderLoad();
			}
			this.renderers.addAll(this.createSet);
			this.createSet.clear();
			this.createSet.addAll(this.cachedSet);
			this.cacheCreate = false;
			this.cachedSet.clear();
		}

		while (!this.destroySet.isEmpty())
		{
			this.cacheDestroy = true;
			for (RenderManager renderer : this.destroySet)
			{
				renderer.onRenderUnload();
			}
			this.renderers.removeAll(this.destroySet);
			this.destroySet.clear();
			this.destroySet.addAll(this.cachedSet);
			this.cacheDestroy = false;
			this.cachedSet.clear();
		}
	}

	public void destroy()
	{
		this.cacheCreate = true;
		this.createSet.clear();
		this.cacheCreate = false;
		this.cachedSet.clear();

		this.destroySet.addAll(this.renderers);
		this.flush();
	}
}
