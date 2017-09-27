package net.jimboi.test.sleuth.util;

import org.bstone.util.small.SmallMap;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Andy on 9/22/17.
 */
public class WeightedSet<E> implements Iterable<E>
{
	private final SmallMap<E, Integer> mapping = new SmallMap<>();

	public void add(E value, int weight)
	{
		this.mapping.put(value, weight);
	}

	public void remove(E value)
	{
		this.mapping.remove(value);
	}

	public E getRandomWeightedValue(Random rand)
	{
		int target = rand.nextInt(this.getTotalWeight());
		int index = 0;
		for(int i = 0; i < this.mapping.size() - 1; ++i)
		{
			index += this.mapping.valueAt(i);
			if (index >= target)
			{
				return this.mapping.keyAt(i);
			}
		}

		return this.mapping.keyAt(this.mapping.size() - 1);
	}

	public int getWeight(E value)
	{
		return this.mapping.get(value);
	}

	public int getTotalWeight()
	{
		int sum = 0;
		for(Integer i : this.mapping.values())
		{
			sum += i;
		}
		return sum;
	}

	@Override
	public Iterator<E> iterator()
	{
		return this.mapping.keySet().iterator();
	}
}
