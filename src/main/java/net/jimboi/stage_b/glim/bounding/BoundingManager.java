package net.jimboi.stage_b.glim.bounding;

import org.bstone.util.listener.Listenable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 6/4/17.
 */
public class BoundingManager
{
	public interface OnBoundingAddListener
	{
		void onBoundingAdd(Bounding bounding);
	}

	public interface OnBoundingRemoveListener
	{
		void onBoundingRemove(Bounding bounding);
	}

	public final Listenable<OnBoundingAddListener> onBoundingAdd = new Listenable<>(((listener, objects) -> listener.onBoundingAdd((Bounding) objects[0])));
	public final Listenable<OnBoundingRemoveListener> onBoundingRemove = new Listenable<>((listener, objects) -> listener.onBoundingRemove((Bounding) objects[0]));

	private Set<Bounding> boundings = new HashSet<>();

	public Bounding create(Bounding bounding)
	{
		this.boundings.add(bounding);
		this.onBoundingAdd.notifyListeners(bounding);
		return bounding;
	}

	public void destroy(Bounding bounding)
	{
		this.boundings.remove(bounding);
		this.onBoundingRemove.notifyListeners(bounding);
	}

	public IntersectionData checkIntersection(Bounding bounding, Vector3fc offset)
	{
		bounding.offset(offset.x(), offset.y(), offset.z());

		IntersectionData data = null;
		for (Bounding other : this.boundings)
		{
			if (other == bounding) continue;
			IntersectionData d = Intersection.test(other, bounding);
			//TODO: this is a hack to solve sweeping intersections!
			if (d != null)
			{
				if (data == null)
				{
					data = d;
				}
				else
				{
					((Vector3f) data.delta).add(d.delta).div(2F);
				}
			}
		}

		bounding.offset(-offset.x(), -offset.y(), -offset.z());
		return data;
	}
}
