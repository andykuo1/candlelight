package net.jimboi.mod.renderer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 5/30/17.
 */
public class RenderEngine
{
	private final Set<Renderer> createSet = new HashSet<>();
	private final Set<Renderer> destroySet = new HashSet<>();
	private final Set<Renderer> renderers = new HashSet<>();

	private final Set<Renderer> cachedSet = new HashSet<>();
	private boolean cacheCreate = false;
	private boolean cacheDestroy = false;

	public void add(Renderer renderer)
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

	public void remove(Renderer renderer)
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

		for (Renderer renderer : this.renderers)
		{
			renderer.onRenderUpdate();
			renderer.onRenderUpdate.notifyListeners();
		}
	}

	public void flush()
	{
		while (!this.createSet.isEmpty())
		{
			this.cacheCreate = true;
			for (Renderer renderer : this.createSet)
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
			for (Renderer renderer : this.destroySet)
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
		for (Renderer renderer : this.renderers)
		{
			renderer.onRenderUnload();
		}

		this.cacheCreate = true;
		this.createSet.clear();
		this.cacheCreate = false;
		this.cachedSet.clear();

		this.flush();
	}
}
