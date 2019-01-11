package canary.test.pye;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Andy on 10/17/17.
 */
public class PetriDish
{
	private final Set<Matter> matters = new HashSet<>();
	private final Queue<Matter> cached = new LinkedList<>();

	public void update()
	{
		while(!this.cached.isEmpty())
		{
			Matter matter = this.cached.poll();
			matter.onCreate(this);
			this.matters.add(matter);
		}

		Iterator<Matter> iter = this.matters.iterator();
		while(iter.hasNext())
		{
			Matter matter = iter.next();
			if (matter.isDead())
			{
				matter.onDestroy(this);
				iter.remove();
			}
			else
			{
				matter.onUpdate(this);
			}
		}

		iter = this.matters.iterator();
		while(iter.hasNext())
		{
			Matter matter = iter.next();
			matter.onMotionUpdate(this);
		}

		iter = this.matters.iterator();
		while(iter.hasNext())
		{
			Matter matter = iter.next();
			matter.onCollisionUpdate(this.matters);
		}
	}

	public void destroy()
	{
		Iterator<Matter> iter = this.matters.iterator();
		while(iter.hasNext())
		{
			Matter matter = iter.next();
			matter.onDestroy(this);
			iter.remove();
		}
		this.cached.clear();
	}

	public void addMatter(Matter matter)
	{
		this.matters.add(matter);
	}

	public void deleteMatter(Matter matter)
	{
		matter.setDead();
	}

	public Iterable<Matter> getMatters()
	{
		return this.matters;
	}

	public Matter getFirstMatterAtPos(float x, float y)
	{
		for(Matter matter : this.matters)
		{
			float dx = matter.posX - x;
			float dy = matter.posY - y;
			float distSqu = dx * dx + dy * dy;
			if (distSqu <= matter.radius * matter.radius)
			{
				return matter;
			}
		}

		return null;
	}

	public float getMotionFriction(float x, float y)
	{
		return 0.996F;
	}

	public float getAngularFriction()
	{
		return 0.996F;
	}
}
