package canary.test.gumshoe.test.opportunity.inspection.simulation.bti.policy;

import java.util.Random;

/**
 * Created by Andy on 12/18/17.
 */
public final class RandomIndexIterator extends FixedIndexIterator
{
	protected final Random rand;

	public RandomIndexIterator(int length, Random rand)
	{
		super(length);

		this.rand = rand;

		for(int i = 0; i < this.indices.length; ++i)
		{
			this.indices[i] = i;
		}

		for(int i = 0; i < this.indices.length; ++i)
		{
			int j = this.indices[i];
			int k = rand.nextInt(this.indices.length);
			this.indices[i] = this.indices[k];
			this.indices[k] = j;
		}
	}
}
