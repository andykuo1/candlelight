package net.jimboi.test.pye;

import org.qsilver.util.MathUtil;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 10/15/17.
 */
public class GameWorld
{
	private final Set<GameObject> objects = new HashSet<>();
	private final Set<GameObject> cached = new HashSet<>();
	private volatile boolean usecache = true;

	public GameObject addGameObject(GameObject go)
	{
		if (this.usecache)
		{
			this.cached.add(go);
		}
		else
		{
			go.setWorld(this);
			go.onCreate();
			this.objects.add(go);
		}
		return go;
	}

	public void update()
	{
		this.usecache = false;
		{
			for (GameObject obj : this.cached)
			{
				this.addGameObject(obj);
			}
			this.cached.clear();
		}
		this.usecache = true;

		Iterator<GameObject> iter = this.objects.iterator();
		while(iter.hasNext())
		{
			GameObject obj = iter.next();
			if (obj.isDead())
			{
				obj.onDestroy();
				iter.remove();
			}
			else
			{
				obj.onUpdate();
			}
		}
	}

	public void render(Graphics g)
	{
		for(GameObject obj : this.objects)
		{
			obj.onRender(g);
		}
	}

	public void solveCollision(GameEntity entity)
	{
		float dx = -1;
		float dy = -1;
		float dd = -1;

		for(GameObject obj : this.objects)
		{
			if (obj instanceof GameEntity && obj != entity)
			{
				GameEntity other = (GameEntity) obj;
				float min = entity.size + other.size;
				float dist = distanceSqu(entity.x, entity.y, other.x, other.y);
				if (other.solid && dist <= min * min)
				{
					if (dd == -1 || dist < dd)
					{
						dd = dist;
						dx = other.x - entity.x;
						dx = MathUtil.sign(dx) * (min - Math.abs(dx));
						dy = other.y - entity.y;
						dy = MathUtil.sign(dy) * (min - Math.abs(dy));
					}
				}
			}
		}

		if (dd != -1)
		{
			entity.x -= dx;
			entity.y -= dy;
		}
	}

	public float distanceSqu(float x1, float y1, float x2, float y2)
	{
		float dy = y2 - y1;
		float dx = x2 - x1;
		return dy * dy + dx * dx;
	}
}

