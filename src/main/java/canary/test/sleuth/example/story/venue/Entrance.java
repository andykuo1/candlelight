package canary.test.sleuth.example.story.venue;

/**
 * Created by Andy on 10/8/17.
 */
public final class Entrance
{
	Room src;
	Room dst;

	Entrance() {}

	public boolean isDestination(Room room)
	{
		return room.equals(this.src) || room.equals(this.dst);
	}

	/**
	 * Assumes the passed-in src is a valid destination of this entrance.
	 */
	public Room getDestination(Room src)
	{
		return src.equals(this.src) ? this.dst : this.src;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Entrance &&
				((this.src.equals(((Entrance) o).src) && this.dst.equals(((Entrance) o).dst)) ||
						(this.src == (((Entrance) o).dst) && this.dst == (((Entrance) o).src)));
	}
}
