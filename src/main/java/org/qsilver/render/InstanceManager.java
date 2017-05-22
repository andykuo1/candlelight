package org.qsilver.render;

import org.bstone.util.listener.Listenable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Andy on 4/30/17.
 */
public class InstanceManager
{
	public interface OnInstanceAddListener
	{
		void onInstanceAdd(InstanceHandler parent, Instance instance);
	}

	public interface OnInstanceRemoveListener
	{
		void onInstanceRemove(InstanceHandler parent, Instance instance);
	}

	public final Listenable<OnInstanceAddListener> onInstanceAdd = new Listenable<>((listener, objects) -> listener.onInstanceAdd((InstanceHandler) objects[0], (Instance) objects[1]));
	public final Listenable<OnInstanceRemoveListener> onInstanceRemove = new Listenable<>((listener, objects) -> listener.onInstanceRemove((InstanceHandler) objects[0], (Instance) objects[1]));

	private class InstanceEntry
	{
		final InstanceHandler parent;
		final Instance instance;

		InstanceEntry(InstanceHandler parent, Instance instance)
		{
			this.parent = parent;
			this.instance = instance;
		}
	}

	private final InstanceHandler defaultHandler = new InstanceHandler()
	{
		@Override
		public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
		{
		}

		@Override
		public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
		{
		}

		@Override
		public void onInstanceDestroy(InstanceManager instanceManager, Instance instance)
		{
		}
	};

	private final Set<InstanceEntry> instances = new HashSet<>();
	private final Set<InstanceHandler> spawnList = new HashSet<>();
	private final Set<InstanceHandler> spawnCache = new HashSet<>();
	private final Set<Instance> defaultSpawn = new HashSet<>();
	private final Set<InstanceEntry> destroyList = new HashSet<>();
	private boolean cached = false;

	private Function<Instance, Void> renderFunction;

	public InstanceManager(Function<Instance, Void> function)
	{
		this.renderFunction = function;
	}

	public void add(InstanceHandler parent)
	{
		if (this.cached)
		{
			this.spawnCache.add(parent);
		}
		else
		{
			this.spawnList.add(parent);
		}
	}

	public void add(Instance instance)
	{
		this.defaultSpawn.add(instance);
	}

	public void update()
	{
		this.flushCreateList();

		for(InstanceEntry entry : this.instances)
		{
			Instance inst = entry.instance;
			if (!inst.isDead())
			{
				entry.parent.onInstanceUpdate(this, inst);

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

		for(InstanceEntry entry : this.instances)
		{
			Instance inst = entry.instance;
			if (inst.isDead())
			{
				this.destroyList.add(entry);
			}
		}

		this.flushDestroyList();
	}

	public void destroyAll()
	{
		for(InstanceEntry entry : this.instances)
		{
			entry.parent.onInstanceDestroy(this, entry.instance);
		}

		this.flushDestroyList();

		this.cached = true;
		this.spawnList.clear();
		this.cached = false;
		this.spawnCache.clear();
		this.defaultSpawn.clear();
	}

	public void clearAll()
	{
		this.cached = true;
		this.spawnList.clear();
		this.cached = false;
		this.spawnCache.clear();
		this.defaultSpawn.clear();
		this.instances.clear();

		this.destroyList.clear();
	}

	public Iterator<Instance> getInstanceIterator()
	{
		final Iterator<InstanceEntry> entryIterator = this.instances.iterator();
		return new Iterator<Instance>()
		{
			@Override
			public boolean hasNext()
			{
				return entryIterator.hasNext();
			}

			@Override
			public Instance next()
			{
				InstanceEntry entry = entryIterator.next();
				return entry.instance;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public void forEachRemaining(Consumer<? super Instance> action)
			{
				entryIterator.forEachRemaining((entry) -> action.accept(entry.instance));
			}
		};
	}

	private static final List<Instance> _LIST = new ArrayList<>();

	private void flushCreateList()
	{
		while(!this.spawnList.isEmpty())
		{
			this.cached = true;
			for (InstanceHandler parent : this.spawnList)
			{
				_LIST.clear();
				parent.onInstanceSetup(this, _LIST);
				if (!_LIST.isEmpty())
				{
					for(Instance instance : _LIST)
					{
						InstanceEntry entry = new InstanceEntry(parent, instance);
						this.instances.add(entry);

						this.onInstanceAdd.notifyListeners(entry.parent, entry.instance);
					}
				}
			}
			this.spawnList.clear();
			this.cached = false;

			this.spawnList.addAll(this.spawnCache);
			this.spawnCache.clear();
		}

		for(Instance inst : this.defaultSpawn)
		{
			this.instances.add(new InstanceEntry(this.defaultHandler, inst));
		}
		this.defaultSpawn.clear();
	}

	private void flushDestroyList()
	{
		for(InstanceEntry entry : this.destroyList)
		{
			entry.parent.onInstanceDestroy(this, entry.instance);
			this.instances.remove(entry);

			this.onInstanceRemove.notifyListeners(entry.parent, entry.instance);
		}

		this.destroyList.clear();
	}
}
