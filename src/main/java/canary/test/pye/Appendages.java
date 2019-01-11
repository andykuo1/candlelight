package canary.test.pye;

import org.joml.Vector2f;

/**
 * Created by Andy on 10/16/17.
 */
public class Appendages
{
	public static final Appendage THRUSTER = new Appendage("thruster", 0,
			(world, owner, index, amt) -> {
				owner.moveToward(-owner.getDirection(index), amt);
				return 0;
			});

	public static final Appendage TURNER = new Appendage("turner", 1,
			(world, owner, index, amt) -> {
				owner.motionRot += amt;
				return 0;
			});

	public static final Appendage SIGHT = new Appendage("sight", 2,
			(world, owner, index, amt) -> {
				float dir = owner.getDirection(index);
				Vector2f look = owner.getPosition(index, new Vector2f());
				look.add((float) Math.cos(dir) * amt, (float) Math.sin(dir) * amt);
				Matter other = world.getFirstMatterAtPos(look.x(), look.y());

				if (other == null) return amt;

				float dx = look.x() - other.posX;
				float dy = look.y() - other.posY;
				return (float) Math.sqrt(dx * dx + dy * dy);
			});

	public static Appendage getRandomAppendage()
	{
		int index = (int) (Math.random() * 4);
		switch (index)
		{
			case 0: return THRUSTER;
			case 1: return TURNER;
			case 2: return SIGHT;
			default: return null;
		}
	}

	public static final int NUM_OF_INPUTS = 1;
	public static final int NUM_OF_OUTPUTS = 1;
}
