package canary.bstone.util.pair;

import java.util.Objects;

/**
 * Created by Andy on 10/7/17.
 */
public class UnorderedPair<E> extends Pair<E, E>
{
	public UnorderedPair(E var1, E var2)
	{
		super(var1, var2);
	}

	public boolean contains(E var)
	{
		return var == this.fst ||
				this.fst != null && this.fst.equals(var) ||
				var == this.snd ||
				this.snd != null && this.snd.equals(var);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Pair)
		{
			if ((Objects.equals(this.fst, ((UnorderedPair) o).fst)
					&& Objects.equals(this.snd, ((UnorderedPair) o).snd))
					|| (Objects.equals(this.fst, ((UnorderedPair) o).snd)
					&& Objects.equals(this.snd, ((UnorderedPair) o).fst)))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.fst == null ? (this.snd == null ? 0 : this.snd.hashCode() + 1) : (this.snd == null ? this.fst.hashCode() + 1 : this.fst.hashCode() + this.snd.hashCode());
	}
}
