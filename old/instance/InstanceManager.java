package org.qsilver.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

/**
 * Created by Andy on 4/30/17.
 */
public class InstanceManager
{
	public interface Listener
	{
		void onInstanceAdd(Instance instance);
		void onInstanceRemove(Instance instance);
	}

	private final List<Instance> spawnList = new ArrayList<>();
	private final List<Instance> spawnCache = new ArrayList<>();
	private final List<Instance> destroyList = new ArrayList<>();
	private final List<Instance> instances = new ArrayList<>();
	private Function<Instance, Void> renderFunction;
	private volatile boolean cached = false;

	private final Listener listener;

	public InstanceManager(Listener listener, Function<Instance, Void> function)
	{
		this.listener = listener;
		this.renderFunction = function;
	}

	public Instance add(Instance inst)
	{
		if (!this.cached)
		{
			this.spawnList.add(inst);
		}
		else
		{
			this.spawnCache.add(inst);
		}

		return inst;
	}

	public void update()
	{
		this.flushCreateList();

		for(Instance inst : this.instances)
		{
			if (!inst.isDead())
			{
				inst.onUpdate();

				if (inst.isVisible())
				{
					this.renderFunction.apply(inst);
				}
			}
		}

		this.flushDestroyList();
	}

	public void flushAll()
	{
		this.flushCreateList();

		for(Instance inst : this.instances)
		{
			if (inst.isDead())
			{
				this.destroyList.add(inst);
			}
		}

		this.flushDestroyList();
	}

	public void destroyAll()
	{
		for(Instance inst : this.instances)
		{
			inst.onDestroy();
		}

		this.flushDestroyList();

		this.cached = true;
		this.spawnList.clear();
		this.cached = false;
		this.spawnCache.clear();
	}

	public void clearAll()
	{
		this.cached = true;
		this.spawnList.clear();
		this.cached = false;
		this.spawnCache.clear();
		this.instances.clear();

		this.destroyList.clear();
	}

	public ListIterator<Instance> getInstanceIterator()
	{
		return this.instances.listIterator();
	}

	private void flushCreateList()
	{
		while(!this.spawnList.isEmpty())
		{
			this.cached = true;
			for (Instance inst : this.spawnList)
			{
				if (inst.onCreate())
				{
					this.instances.add(inst);
					this.onAddInstance(inst);
				}
			}
			this.spawnList.clear();
			this.cached = false;

			this.spawnList.addAll(this.spawnCache);
			this.spawnCache.clear();
		}
	}

	private void flushDestroyList()
	{
		for(Instance inst : this.destroyList)
		{
			inst.onDestroy();
			this.instances.remove(inst);
			this.onRemoveInstance(inst);
		}

		this.destroyList.clear();
	}

	private void onAddInstance(Instance instance)
	{
		if (this.listener != null)
		{
			this.listener.onInstanceAdd(instance);
		}
	}

	private void onRemoveInstance(Instance instance)
	{
		if (this.listener != null)
		{
			this.listener.onInstanceRemove(instance);
		}
	}
}
