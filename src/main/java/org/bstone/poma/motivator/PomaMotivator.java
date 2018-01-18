package org.bstone.poma.motivator;

/**
 * Created by Andy on 6/10/17.
 */
public final class PomaMotivator
{
	/*
	private static final String PRINT_DIVIDER = "-=(*)=-";
	//TODO: Should print motivation if a threshold is met from printing too much useless junk . . .
	//TODO: Should print appropriately for repeated prints vs repeated exceptions. . .

	private static final List<PomaMotivator> MOTIVATORS = new ArrayList<>();
	private static final Set<Consumer<Object>> NONDIMINISHING_RETURNS = new HashSet<>();
	private static final Set<Object> CAKE_OVEN = new HashSet<>();
	private static final List<Integer> LITTLE_HOUSE_OF_INTS = new ArrayList<>();
	private static final Random SIR_RANDY = new Random();
	private static final float CONCENTRATION_FACTOR = 0.8F;
	private static final float SADNESS_THRESHOLD = 0.5F;
	private static final int BOREDOM_CONSTANT = 4;
	private static final float BRICKWALL_INTELLIGENCE = 0.1F;
	private static final float EXPLOSION_RATIO = 0.04F;

	private static final boolean isDeveloperConcentrating()
	{
		return SIR_RANDY.nextFloat() <= CONCENTRATION_FACTOR;
	}

	private static final float getGroupIntensity()
	{
		float intensity = 0;
		synchronized (MOTIVATORS)
		{
			for (PomaMotivator motivator : MOTIVATORS)
			{
				intensity += motivator.intensity;
			}
		}
		return intensity;
	}

	public static void motivate()
	{
		if (!isDeveloperConcentrating())
		{
			float dumbness = 0;
			synchronized (MOTIVATORS)
			{
				for (PomaMotivator motivator : MOTIVATORS)
				{
					float awareness = motivator.getAwareness();
					dumbness += awareness;
				}
				dumbness /= MOTIVATORS.size();
			}

			if (dumbness > SADNESS_THRESHOLD)
			{
				if (dumbness / BRICKWALL_INTELLIGENCE > SADNESS_THRESHOLD)
				{
					injectRemainingMotivation(getGroupIntensity() * SIR_RANDY.nextFloat());
				}
				else
				{
					waitForMotivation();
				}
				return;
			}
		}
		else
		{
			//Stay concentrated! ;)
			giveMoreCake();
		}
	}

	private static void giveMoreCake()
	{
		CAKE_OVEN.add("To be or not to be.");
	}

	private static Integer findMoreMotivation()
	{
		final int i = NONDIMINISHING_RETURNS.size();
		NONDIMINISHING_RETURNS.add((cake) ->
		{
			Poma.div();
			Poma.info("\"Motivation?\"");
			Poma.info("            - No One #" + (i + cake.hashCode()));
			Poma.div();
		});
		return i;
	}

	private static Object findRandomCake()
	{
		Object cake = null;
		synchronized (CAKE_OVEN)
		{
			int i = SIR_RANDY.nextInt(CAKE_OVEN.size());
			Iterator<Object> iter = CAKE_OVEN.iterator();
			while (iter.hasNext())
			{
				cake = iter.next();
				if (--i <= 0) break;
			}
		}
		return cake;
	}

	private static void waitForMotivation()
	{
		int i = SIR_RANDY.nextInt(NONDIMINISHING_RETURNS.size());
		LITTLE_HOUSE_OF_INTS.add(i);
		int ifreq = Collections.frequency(LITTLE_HOUSE_OF_INTS, i);
		if (ifreq >= BOREDOM_CONSTANT)
		{
			i = findMoreMotivation();
			LITTLE_HOUSE_OF_INTS.add(i);
		}

		Object cake = findRandomCake();
		Consumer<Object> consumer = pick(NONDIMINISHING_RETURNS, i);
		consumer.accept(cake);
	}

	private static void injectRemainingMotivation(float intensity)
	{
		//TODO: Should grab immediately for super motivation!
		waitForMotivation();
	}

	private static <T> T pick(Collection<T> collection)
	{
		int i = SIR_RANDY.nextInt(collection.size());
		for (T t : collection)
		{
			--i;
			if (i <= 0) return t;
		}

		return collection.iterator().next();
	}

	private static <T> T pick(Collection<T> collection, int i)
	{
		for (T t : collection)
		{
			--i;
			if (i <= 0) return t;
		}

		return pick(collection);
	}

	private float intensity;
	private float capacity;
	private float awareness;

	public PomaMotivator(float intensity, float capacity)
	{
		this.intensity = intensity;
		this.capacity = capacity;
		this.awareness = 0;

		MOTIVATORS.add(this);
	}

	public void poke()
	{
		Poma.debug("...Poked...");
		this.awareness += this.intensity;
		if (this.awareness > this.capacity)
		{
			this.awareness = this.capacity;
		}
		Poma.debug("Triggered: " + this.awareness + "/" + this.capacity);

		if (SIR_RANDY.nextFloat() < EXPLOSION_RATIO)
		{
			Poma.debug("...Motivating...");
			motivate();
		}
		else
		{
			Poma.debug("...Nothing...");
		}
	}

	public void slap(float force)
	{
		this.awareness += force * this.intensity;
		if (this.awareness > this.capacity)
		{
			this.awareness = this.capacity;
		}
	}

	public void dump()
	{
		this.awareness = 0;
	}

	public boolean isGettingTired()
	{
		return this.awareness >= this.capacity;
	}

	public float getAwareness()
	{
		return this.awareness;
	}
	*/
}
