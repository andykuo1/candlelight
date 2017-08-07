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
	public interface OnLivingCreateListener<L extends Living>
	{
		void onLivingCreate(L living);
	}

	public interface OnLivingDestroyListener<L extends Living>
	{
		void onLivingDestroy(L living);
	}

	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingCreateListener<L>> onLivingCreate = new Listenable<>((listener, objects) -> listener.onLivingCreate((L) objects[0]));

	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingDestroyListener<L>> onLivingDestroy = new Listenable<>(((listener, objects) -> listener.onLivingDestroy((L) objects[0])));

	private int NEXT_LIVING_ID = 0;
	private final List<L> createQueue = new ArrayList<>();
	private final List<L> createCache = new ArrayList<>();
	private final Map<Integer, L> livings = new HashMap<>();
	private volatile boolean cached = false;

	public L add(L living)
	{
		if (!this.cached)
		{
			this.createQueue.add(living);
		}
		else
		{
			this.createCache.add(living);
		}

		return living;
	}

	public void update()
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
				living.onUpdate();
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
				this.onDestroyLiving(living);
			}
		}
	}

	public void flush(boolean doDead)
	{
		while(!this.createQueue.isEmpty())
		{
			this.cached = true;
			for (L living : this.createQueue)
			{
				if (living.onCreate())
				{
					int id = this.getNextLivingID();
					living.id = id;
					this.livings.put(id, living);
					this.onCreateLiving(living);
				}
			}
			this.createQueue.clear();
			this.cached = false;

			this.createQueue.addAll(this.createCache);
			this.createCache.clear();
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
					this.onDestroyLiving(living);
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
			this.onDestroyLiving(living);
		}

		this.cached = true;
		this.createQueue.clear();
		this.cached = false;
		this.createCache.clear();
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

	private void onCreateLiving(L living)
	{
		this.onLivingCreate.notifyListeners(living);
	}

	private void onDestroyLiving(L living)
	{
		this.onLivingDestroy.notifyListeners(living);
	}
}
