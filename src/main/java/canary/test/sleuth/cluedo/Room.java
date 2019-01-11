package canary.test.sleuth.cluedo;

/**
 * Created by Andy on 9/23/17.
 */
public class Room
{
	public final RoomType type;

	public Room(RoomType type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return this.type.toString();
	}
}
