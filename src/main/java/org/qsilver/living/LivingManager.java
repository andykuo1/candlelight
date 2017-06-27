package org.qsilver.living;

import org.bstone.util.listener.Listenable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	private int NEXT_LIVING_ID = 0;
	private final List<Living> spawnList = new ArrayList<>();
	private final List<Living> spawnCache = new ArrayList<>();
	private final Map<Integer, Living> livings = new HashMap<>();
	private volatile boolean cached = false;

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
		this.flush(false);

		for(Living living : this.livings.values())
		{
			if (!living.isDead())
			{
				living.onEarlyUpdate();
			}
		}

		for(Living living : this.livings.values())
		{
			if (!living.isDead())
			{
				living.onUpdate(delta);
			}
		}

		Iterator<Living> iter = this.livings.values().iterator();
		while(iter.hasNext())
		{
			Living living = iter.next();
			if (!living.isDead())
			{
				living.onLateUpdate();
			}
			else
			{
				living.onDestroy();
				iter.remove();
				this.onRemoveLiving(living);
			}
		}
	}

	public void flush(boolean doDead)
	{
		while(!this.spawnList.isEmpty())
		{
			this.cached = true;
			for (Living living : this.spawnList)
			{
				if (living.onCreate())
				{
					int id = this.getNextLivingID();
					living.id = id;
					this.livings.put(id, living);
					this.onAddLiving(living);
				}
			}
			this.spawnList.clear();
			this.cached = false;

			this.spawnList.addAll(this.spawnCache);
			this.spawnCache.clear();
		}

		if (doDead)
		{
			Iterator<Living> iter = this.livings.values().iterator();
			while (iter.hasNext())
			{
				Living living = iter.next();
				if (living.isDead())
				{
					living.onDestroy();
					iter.remove();
					this.onRemoveLiving(living);
				}
			}
		}
	}

	public void destroyAll()
	{
		Iterator<Living> iter = this.livings.values().iterator();
		while(iter.hasNext())
		{
			Living living = iter.next();
			living.setDead();
			living.onDestroy();
			iter.remove();
			this.onRemoveLiving(living);
		}

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
	}

	public Iterator<Living> getLivingIterator()
	{
		return this.livings.values().iterator();
	}

	private int getNextLivingID()
	{
		return NEXT_LIVING_ID++;
	}

	private void onAddLiving(Living living)
	{
		this.onLivingAdd.notifyListeners(living);
	}

	private void onRemoveLiving(Living living)
	{
		this.onLivingRemove.notifyListeners(living);
	}
}
