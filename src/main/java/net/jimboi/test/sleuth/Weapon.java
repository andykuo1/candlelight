package net.jimboi.test.sleuth;

import net.jimboi.test.sleuth.comm.Environment;

/**
 * Created by Andy on 9/23/17.
 */
public abstract class Weapon
{
	public static final Weapon KNIFE = new Weapon("Knife", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			//What body part to aim for?
			//If the person knows to aim for the heart
			//Otherwise, start with region, then randomize according to knowledge
			//BodyPart part = getBodyPartToAimFor();
			//BodyPart targetPart = attemptAim(accuracy, part);
			//doDamage(targetPart);

			return false;
		}
	};

	public static final Weapon BLUNT = new Weapon("Blunt", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon FIREARM = new Weapon("Firearm", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon UNARMED = new Weapon("Unarmed", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon ROPE = new Weapon("Strangulation", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon PILLOW = new Weapon("Suffocation", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon POISON = new Weapon("Poison", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon EXPLOSIVE = new Weapon("Explosive", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon BURNING = new Weapon("Burning", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public static final Weapon DROWNING = new Weapon("Drowning", "Melee")
	{
		@Override
		public boolean use(Actor user, Actor target, Environment environment)
		{
			return false;
		}
	};

	public String name;
	public String type;

	public Weapon(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	public abstract boolean use(Actor user, Actor target, Environment environment);

	@Override
	public String toString()
	{
		return this.name;
	}
}
