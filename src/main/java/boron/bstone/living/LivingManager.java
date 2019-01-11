package boron.bstone.living;

import boron.bstone.util.listener.Listenable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 8/12/17.
 */
public class LivingManager<L extends ILiving>
{
	public interface OnLivingCreateListener<L extends ILiving>
	{
		void onLivingCreate(L living);
	}

	public interface OnLivingDestroyListener<L extends ILiving>
	{
		void onLivingDestroy(L living);
	}

	public interface OnUpdateLivingsListener<L extends ILiving>
	{
		void onUpdateLivings(LivingManager<L> livingManager);
	}

	public interface OnLateUpdateLivingsListener<L extends ILiving>
	{
		void onLateUpdateLivings(LivingManager<L> livingManager);
	}

	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingCreateListener<L>> onLivingCreate
			= new Listenable<>((listener, objects) -> listener.onLivingCreate((L) objects[0]));
	@SuppressWarnings("unchecked")
	public final Listenable<OnLivingDestroyListener<L>> onLivingDestroy
			= new Listenable<>(((listener, objects) -> listener.onLivingDestroy((L) objects[0])));
	@SuppressWarnings("unchecked")
	public final Listenable<OnUpdateLivingsListener<L>> onUpdateLivings
			= new Listenable<>(((listener, objects) -> listener.onUpdateLivings((LivingManager<L>) objects[0])));
	@SuppressWarnings("unchecked")
	public final Listenable<OnLateUpdateLivingsListener<L>> onLateUpdateLivings
			= new Listenable<>(((listener, objects) -> listener.onLateUpdateLivings((LivingManager<L>) objects[0])));

	private int NEXT_LIVING_ID = 0;

	private final List<L> createQueue = new ArrayList<>();
	private final List<L> createCache = new ArrayList<>();
	private final Map<Integer, L> livings = new HashMap<>();
	private volatile boolean cached = false;

	public void update()
	{
		this.flush(false);

		for(L living : this.livings.values())
		{
			if (!living.isDead())
			{
				living.onLivingUpdate();
			}
		}
		this.onUpdateLivings.notifyListeners(this);

		Iterator<L> livings = this.livings.values().iterator();
		while(livings.hasNext())
		{
			L living = livings.next();
			if (!living.isDead())
			{
				living.onLivingLateUpdate();
			}
			else
			{
				living.onLivingDestroy();

				this.onLivingDestroy.notifyListeners(living);

				livings.remove();

				living.setLivingManager(null);
				living.setLivingID(-1);
			}
		}
		this.onLateUpdateLivings.notifyListeners(this);
	}

	public L addLiving(L living)
	{
		if (this.cached)
		{
			this.createCache.add(living);
		}
		else
		{
			this.createQueue.add(living);
		}
		return living;
	}

	public void flush(boolean doDestroy)
	{
		while(!this.createQueue.isEmpty())
		{
			this.cached = true;

			for(L living : this.createQueue)
			{
				int id = this.getNextAvailableLivingID();
				living.setLivingID(id);
				living.setLivingManager(this);

				this.livings.put(living.getLivingID(), living);

				living.onLivingCreate(this);

				this.onLivingCreate.notifyListeners(living);
			}
			this.createQueue.clear();

			this.cached = false;

			this.createQueue.addAll(this.createCache);
			this.createCache.clear();
		}

		if (doDestroy)
		{
			Iterator<L> livings = this.livings.values().iterator();
			while(livings.hasNext())
			{
				L living = livings.next();
				if (living.isDead())
				{
					living.onLivingDestroy();

					this.onLivingDestroy.notifyListeners(living);

					livings.remove();

					living.setLivingManager(null);
					living.setLivingID(-1);
				}
			}
		}
	}

	public void clear()
	{
		this.createCache.clear();
		this.createQueue.clear();
		this.livings.clear();
	}

	public Iterable<L> getLivings()
	{
		return this.livings.values();
	}

	public int getNextAvailableLivingID()
	{
		return NEXT_LIVING_ID++;
	}
}
