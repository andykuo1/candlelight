package org.bstone.living;

import org.bstone.util.listener.Listenable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 3/4/17.
 */
public class LivingManager<L extends Living>
{
	public interface OnLivingAddListener<L extends Living>
	{
		void onLivingAdd(L living);
	}

	public interface OnLivingRemoveListener<L extends Living>
	{
		void onLivingRemove(L living);
	}

	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingAddListener<L>> onLivingAdd = new Listenable<>((listener, objects) -> listener.onLivingAdd((L) objects[0]));

	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingRemoveListener<L>> onLivingRemove = new Listenable<>(((listener, objects) -> listener.onLivingRemove((L) objects[0])));

	private int NEXT_LIVING_ID = 0;
	private final List<L> spawnList = new ArrayList<>();
	private final List<L> spawnCache = new ArrayList<>();
	private final Map<Integer, L> livings = new HashMap<>();
	private volatile boolean cached = false;

	public L add(L living)
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

		Iterator<L> iter = this.livings.values().iterator();
		while(iter.hasNext())
		{
			L living = iter.next();
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
			for (L living : this.spawnList)
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
			Iterator<L> iter = this.livings.values().iterator();
			while (iter.hasNext())
			{
				L living = iter.next();
				if (living.isDead())
				{
					living.onDestroy();
					iter.remove();
					this.onRemoveLiving(living);
				}
			}
		}
	}

	public void destroy()
	{
		Iterator<L> iter = this.livings.values().iterator();
		while(iter.hasNext())
		{
			L living = iter.next();
			living.setDead();
			living.onDestroy();
			iter.remove();
			this.onRemoveLiving(living);
		}

		this.cached = true;
		this.spawnList.clear();
		this.cached = false;
		this.spawnCache.clear();
		this.livings.clear();
	}

	public Iterator<L> getLivingIterator()
	{
		return this.livings.values().iterator();
	}

	private int getNextLivingID()
	{
		return NEXT_LIVING_ID++;
	}

	private void onAddLiving(L living)
	{
		this.onLivingAdd.notifyListeners(living);
	}

	private void onRemoveLiving(L living)
	{
		this.onLivingRemove.notifyListeners(living);
	}
}
