package canary.test.sleuth.cluedo;

import java.util.Random;

/**
 * Created by Andy on 9/24/17.
 */
public class RoomFactory
{
	public static Room create(Random rand, RoomType type)
	{
		return new Room(type);
	}
}
