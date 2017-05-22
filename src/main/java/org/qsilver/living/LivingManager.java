package org.qsilver.living;

import org.bstone.util.listener.Listenable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Andy on 3/4/17.
 */
public class LivingManager
{
	public interface OnLivingAddListener
	{
		void onLivingAdd(Living living);
	}

	public interface OnLivingRemoveListener
	{
		void onLivingRemove(Living living);
	}

	public final Listenable<OnLivingAddListener> onLivingAdd = new Listenable<>((listener, objects) -> listener.onLivingAdd((Living) objects[0]));
	public final Listenable<OnLivingRemoveListener> onLivingRemove = new Listenable<>(((listener, objects) -> listener.onLivingRemove((Living) objects[0])));

	private final List<Living> spawnList = new ArrayList<>();
	private final List<Living> spawnCache = new ArrayList<>();
	private final List<Living> destroyList = new ArrayList<>();
	private final List<Living> livings = new ArrayList<>();
	private volatile boolean cached = false;

	private final Set<LivingSet> livingSets = new HashSet<>();

	class LivingSet<T>
	{
		private final WeakReference<Set<T>> set;
		private final Class<T> type;

		LivingSet(Set<T> set, Class<T> type)
		{
			this.set = new WeakReference<>(set);
			this.type = type;
		}

		boolean hasSet()
		{
			return this.set.get() == null;
		}

		Set<T> getSet()
		{
			return this.set.get();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Set<T> addLivingSet(Set<T> set, Class<T> type)
	{
		LivingSet<T> livingSet = new LivingSet(set, type);
		this.livingSets.add(livingSet);
		return set;
	}

	@SuppressWarnings("unchecked")
	public <T> Set<T> getLivingSet(Class<T> type)
	{
		this.flushLivingSets();

		for(LivingSet livingSet : this.livingSets)
		{
			if (livingSet.type == type)
			{
				Set set = livingSet.getSet();
				if (set != null)
				{
					return set;
				}
			}
		}

		return this.addLivingSet(new HashSet<>(), type);
	}

	public Living add(Living living)
	{
		if (!this.cached)
		{
			this.spawnList.add(living);
		}
		else
		{
			this.spawnCache.add(living);
		}

		return living;
	}

	public void update(double delta)
	{
		this.flushCreateList();

		for(Living living : this.livings)
		{
			if (!living.isDead())
			{
				living.onEarlyUpdate();
			}
		}

		for(Living living : this.livings)
		{
			if (!living.isDead())
			{
				living.onUpdate(delta);
			}
		}

		for(Living living : this.livings)
		{
			if (!living.isDead())
			{
				living.onLateUpdate();
			}
			else
			{
				this.destroyList.add(living);
			}
		}

		this.flushDestroyList();
	}

	public void flushAll()
	{
		this.flushCreateList();

		for(Living living : this.livings)
		{
			if (living.isDead())
			{
				this.destroyList.add(living);
			}
		}

		this.flushDestroyList();
	}

	public void flushLivingSets()
	{
		this.livingSets.removeIf(new Predicate<LivingSet>()
		{
			@Override
			public boolean test(LivingSet livingSet)
			{
				return !livingSet.hasSet();
			}
		});
	}

	public void destroyAll()
	{
		for(Living living : this.livings)
		{
			living.setDead();
			living.onDestroy();
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
		this.livings.clear();

		for(LivingSet livingSet : this.livingSets)
		{
			Set set = livingSet.getSet();
			if (set != null)
			{
				set.clear();
			}
		}

		this.destroyList.clear();
	}

	public ListIterator<Living> getLivingIterator()
	{
		return this.livings.listIterator();
	}

	private void flushCreateList()
	{
		while(!this.spawnList.isEmpty())
		{
			this.cached = true;
			for (Living living : this.spawnList)
			{
				if (living.onCreate())
				{
					this.livings.add(living);
					this.onAddLiving(living);
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
		for(Living living : this.destroyList)
		{
			living.onDestroy();
			this.livings.remove(living);
			this.onRemoveLiving(living);
		}
		this.destroyList.clear();
	}

	@SuppressWarnings("unchecked")
	private void onAddLiving(Living living)
	{
		for(LivingSet livingSet : this.livingSets)
		{
			if (livingSet.type.isInstance(living))
			{
				Set set = livingSet.getSet();
				if (set != null)
				{
					set.add(living);
				}
			}
		}

		this.onLivingAdd.notifyListeners(living);
	}

	private void onRemoveLiving(Living living)
	{
		for(LivingSet livingSet : this.livingSets)
		{
			if (livingSet.type.isInstance(living))
			{
				Set set = livingSet.getSet();
				if (set != null)
				{
					set.remove(living);
				}
			}
		}

		this.onLivingRemove.notifyListeners(living);
	}
}
