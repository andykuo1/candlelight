package canary.bstone.living;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Andy on 8/12/17.
 */
public class LivingManager
{
	private final List<Living> createQueue = new ArrayList<>();
	private final List<Living> createCache = new ArrayList<>();
	private final Set<Living> livings = new HashSet<>();
	private volatile boolean cached = false;

	public void update()
	{
		this.flushCreatedLivings();

		for(Living living : this.livings)
		{
			if (!living.isDead())
			{
				living.onUpdate();
			}
		}
		this.onUpdate();

		Iterator<Living> livings = this.livings.iterator();
		while(livings.hasNext())
		{
			Living living = livings.next();
			if (!living.isDead())
			{
				living.onLateUpdate();
			}
			else
			{
				living.onDestroy();
				this.onLivingDestroy(living);

				livings.remove();
			}
		}
		this.onLateUpdate();
	}

	public Living addLiving(Living living)
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

	public void flushCreatedLivings()
	{
		while(!this.createQueue.isEmpty())
		{
			this.cached = true;

			for(Living living : this.createQueue)
			{
				this.livings.add(living);

				living.onCreate(this);
				this.onLivingCreate(living);
			}
			this.createQueue.clear();

			this.cached = false;

			this.createQueue.addAll(this.createCache);
			this.createCache.clear();
		}
	}

	public void flushDestroyedLivings()
	{
		Iterator<Living> livings = this.livings.iterator();
		while(livings.hasNext())
		{
			Living living = livings.next();
			if (living.isDead())
			{
				living.onDestroy();
				this.onLivingDestroy(living);

				livings.remove();
			}
		}
	}

	public void clear()
	{
		this.createCache.clear();
		this.createQueue.clear();
		this.livings.clear();
	}

	public Iterable<Living> getLivings()
	{
		return this.livings;
	}

	protected void onLivingCreate(Living living)
	{
	}

	protected void onLivingDestroy(Living living)
	{
	}

	protected void onUpdate()
	{
	}

	protected void onLateUpdate()
	{
	}
}
