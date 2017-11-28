package net.jimboi.canary.stage_a.owle;

import org.qsilver.util.iterator.FilterIterator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Andy on 11/21/17.
 */
public class ViewComponentManager
{
	private final Queue<ViewComponent> createQueue = new LinkedList<>();
	private final List<ViewComponent> components = new ArrayList<>();
	private final Queue<ViewComponent> destroyQueue = new LinkedList<>();

	public ViewComponentManager()
	{

	}

	public <T extends ViewComponent> T addComponent(T component)
	{
		this.createQueue.add(component);
		return component;
	}

	public <T extends ViewComponent> T removeComponent(T component)
	{
		this.destroyQueue.add(component);
		return component;
	}

	public boolean hasComponent(ViewComponent component)
	{
		return this.isComponentActive(component) && this.components.contains(component);
	}

	public boolean isComponentActive(ViewComponent component)
	{
		return !this.destroyQueue.contains(component);
	}

	public Iterable<ViewComponent> getActiveComponents()
	{
		return () -> new FilterIterator<>(this.components.iterator(), this::isComponentActive);
	}

	public Iterable<ViewComponent> getComponents()
	{
		return this.components;
	}

	public void update()
	{
		this.flush(true, true);

		for (ViewComponent component : this.components)
		{
			//component.onUpdate();
		}
	}

	public void destroy()
	{
		this.flush(false, true);
		this.destroyQueue.addAll(this.components);
		this.flush(false, true);
		this.createQueue.clear();
	}

	private void flush(boolean create, boolean destroy)
	{
		if (create)
		{
			while (!this.createQueue.isEmpty())
			{
				ViewComponent component = this.createQueue.poll();
				//component.manager = this;
				//component.onCreate(this);
				this.components.add(component);
			}
		}

		if (destroy)
		{
			while (!this.destroyQueue.isEmpty())
			{
				ViewComponent component = this.destroyQueue.poll();
				//component.onDestroy();
				this.components.remove(component);
			}
		}
	}
}
