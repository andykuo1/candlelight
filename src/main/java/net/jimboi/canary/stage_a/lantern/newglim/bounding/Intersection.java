package net.jimboi.canary.stage_a.lantern.newglim.bounding;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Andy on 6/2/17.
 */
public class Intersection
{
	@SuppressWarnings("unchecked")
	public static IntersectionData test(Bounding a, Bounding b)
	{
		IntersectionEntry entry = BOUNDING_ENTRY_FUNCTION.get(BOUNDING_ENTRY_POINTER.set(a, b));
		if (entry != null)
		{
			return entry.test(a, b);
		}

		return null;
	}

	private static final Map<IntersectionEntry, IntersectionEntry> BOUNDING_ENTRY_FUNCTION = new HashMap<>();
	private static final IntersectionEntryPointer BOUNDING_ENTRY_POINTER = new IntersectionEntryPointer(null, null);

	public static <A extends Bounding, B extends Bounding> void register(Class<? extends A> a, Class<? extends B> b, BiFunction<A, B, IntersectionData> intersectionFunction)
	{
		IntersectionEntry entry = new IntersectionEntry<>(a, b, intersectionFunction);
		entry = BOUNDING_ENTRY_FUNCTION.put(entry, entry);
		if (entry != null)
		{
			throw new IllegalStateException("Found duplicate existing BoundingEntry for '" + a + "' + '" + b + "'!");
		}
	}

	private static final class IntersectionEntry<A extends Bounding, B extends Bounding>
	{
		private final Class<? extends A> typeA;
		private final Class<? extends B> typeB;
		private final BiFunction<A, B, IntersectionData> intersectionFunction;

		public IntersectionEntry(Class<? extends A> typeA, Class<? extends B> typeB, BiFunction<A, B, IntersectionData> intersectionFunction)
		{
			this.typeA = typeA;
			this.typeB = typeB;
			this.intersectionFunction = intersectionFunction;
		}

		@SuppressWarnings("unchecked")
		public IntersectionData test(Bounding a, Bounding b)
		{
			return this.intersectionFunction.apply((A) a, (B) b);
		}

		public boolean equals(Class typeA, Class typeB)
		{
			return this.typeA.equals(typeA) && this.typeB.equals(typeB);
		}

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof IntersectionEntry)
			{
				return this.equals(((IntersectionEntry) o).typeA, ((IntersectionEntry) o).typeB);
			}
			else if (o instanceof IntersectionEntryPointer)
			{
				return this.equals(((IntersectionEntryPointer) o).typeA, ((IntersectionEntryPointer) o).typeB);
			}

			return false;
		}

		@Override
		public int hashCode()
		{
			return this.typeA.hashCode() + this.typeB.hashCode();
		}
	}

	private static final class IntersectionEntryPointer
	{
		Class<? extends Bounding> typeA;
		Class<? extends Bounding> typeB;

		public IntersectionEntryPointer(Class<? extends Bounding> typeA, Class<? extends Bounding> typeB)
		{
			this.typeA = typeA;
			this.typeB = typeB;
		}

		public IntersectionEntryPointer set(Bounding a, Bounding b)
		{
			this.typeA = a.getClass();
			this.typeB = b.getClass();
			return this;
		}

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof IntersectionEntry)
			{
				return ((IntersectionEntry) o).equals(this.typeA, this.typeB);
			}

			return false;
		}

		@Override
		public int hashCode()
		{
			return this.typeA.hashCode() + this.typeB.hashCode();
		}
	}
}
