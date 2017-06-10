package org.bstone.util.direction;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.qsilver.poma.Poma;

/**Enum of cardinal directions*/
public enum Direction
{
	EAST,
	NORTHEAST,
	NORTH,
	NORTHWEST,
	WEST,
	SOUTHWEST,
	SOUTH,
	SOUTHEAST,
	CENTER;

	/**Static class for only 4-direction cardinal functions;
	 * <br>Which includes {@link #NORTH}, {@link #SOUTH}, {@link #WEST}, {@link #EAST}*/
	public static class Cardinals
	{
		private Cardinals() {}

		/**Gets the next clockwise {@link Direction} of passed-in direction in only cardinals*/
		public static Direction getClockwise(Direction direction)
		{
			Poma.ASSERT(equals(direction));

			switch(direction)
			{
				case EAST: return Direction.SOUTH;
				case NORTH: return Direction.EAST;
				case WEST: return Direction.NORTH;
				case SOUTH: return Direction.WEST;
				default: return Direction.CENTER;
			}
		}

		/**Gets the next count-clockwise {@link Direction} of passed-in direction in only cardinals*/
		public static Direction getCounterClockwise(Direction direction)
		{
			Poma.ASSERT(equals(direction));

			switch(direction)
			{
				case EAST: return Direction.NORTH;
				case NORTH: return Direction.WEST;
				case WEST: return Direction.SOUTH;
				case SOUTH: return Direction.EAST;
				default: return Direction.CENTER;
			}
		}

		/**Gets the estimated {@link Direction} of passed-in degrees in only cardinals*/
		public static Direction getDirection(double direction)
		{
			direction = (direction + 45 + 360) % 360;
			switch ((int) direction / 90)
			{
				case 0: return Direction.EAST;
				case 1: return Direction.NORTH;
				case 2: return Direction.WEST;
				case 3: return Direction.SOUTH;
				default: return Direction.CENTER;
			}
		}

		public static Direction[] values()
		{
			return new Direction[] {EAST, NORTH, WEST, SOUTH};
		}

		/**Whether the passed-in direction is only cardinal*/
		public static boolean equals(Direction direction)
		{
			return direction == EAST || direction == NORTH || direction == WEST || direction == SOUTH;
		}
	}

	/**Static class for only 4-direction primary inter-cardinal functions*/
	public static class InterCardinals
	{
		private InterCardinals() {}

		public static Direction[] values()
		{
			return new Direction[] {NORTHEAST, NORTHWEST, SOUTHWEST, SOUTHEAST};
		}

		/**Whether the passed-in direction is only inter-cardinal*/
		public static boolean equals(Direction direction)
		{
			return direction == NORTHEAST || direction == NORTHWEST || direction == SOUTHWEST || direction == SOUTHEAST;
		}
	}

	/**Gets the polar {@link Vector2f} representation of direction*/
	public Vector2f polarf(Vector2f dst)
	{
		double dist = 1D;
		double rad = this.radians();
		return dst.set((float)(dist * Math.cos(rad)), (float)(dist * Math.sin(rad)));
	}


	/**Gets the polar {@link Vector2i} representation of direction in only integers*/
	public Vector2i polari(Vector2i dst)
	{
		switch(this)
		{
			case EAST: return dst.set(1, 0);
			case NORTHEAST: return dst.set(1, 1);
			case NORTH: return dst.set(0, 1);
			case NORTHWEST: return dst.set(-1, 1);
			case WEST: return dst.set(-1, 0);
			case SOUTHWEST: return dst.set(-1, -1);
			case SOUTH: return dst.set(0, -1);
			case SOUTHEAST: return dst.set(1, -1);
			default: return dst.set(0, 0);
		}
	}

	/**Directional value in degrees with {@link #EAST} being 0 degrees and increasing counter-clockwise*/
	public double degrees()
	{
		switch(this)
		{
			case EAST: return 0.0D;
			case NORTHEAST: return 45.0D;
			case NORTH: return 90.0D;
			case NORTHWEST: return 135.0D;
			case WEST: return 180.0D;
			case SOUTHWEST: return 225.0D;
			case SOUTH: return 270.0D;
			case SOUTHEAST: return 315.0D;
			default: return 0.0D;
		}
	}

	/**Directional value in radians with {@link #EAST} being 0 radians and increasing counter-clockwise*/
	public double radians()
	{
		return Math.toRadians(degrees());
	}

	/**Gets the opposite directional value*/
	public Direction getOpposite()
	{
		switch(this)
		{
			case EAST: return Direction.WEST;
			case NORTHEAST: return Direction.SOUTHWEST;
			case NORTH: return Direction.SOUTH;
			case NORTHWEST: return Direction.SOUTHEAST;
			case WEST: return Direction.EAST;
			case SOUTHWEST: return Direction.NORTHEAST;
			case SOUTH: return Direction.NORTH;
			case SOUTHEAST: return Direction.NORTHWEST;
			default: return Direction.CENTER;
		}
	}

	/**If direction is relatively north;
	 * <br>Includes: {@link #NORTHWEST}, {@link #NORTH}, {@link #NORTHEAST}
	 * */
	public boolean isNorth()
	{
		return this == Direction.NORTHWEST || this == Direction.NORTH || this == Direction.NORTHEAST;
	}

	/**If direction is relatively south;
	 * <br>Includes: {@link #SOUTHWEST}, {@link #SOUTH}, {@link #SOUTHEAST}
	 * */
	public boolean isSouth()
	{
		return this == Direction.SOUTHWEST || this == Direction.SOUTH || this == Direction.SOUTHEAST;
	}

	/**If direction is relatively west;
	 * <br>Includes: {@link #NORTHWEST}, {@link #WEST}, {@link #SOUTHWEST}
	 * */
	public boolean isWest()
	{
		return this == Direction.NORTHWEST || this == Direction.WEST || this == Direction.SOUTHWEST;
	}

	/**If direction is relatively east;
	 * <br>Includes: {@link #NORTHEAST}, {@link #EAST}, {@link #SOUTHEAST}
	 * */
	public boolean isEast()
	{
		return this == Direction.NORTHEAST || this == Direction.EAST || this == Direction.SOUTHEAST;
	}

	/**If direction is directly north or south*/
	public boolean isNorthSouth()
	{
		return this == Direction.NORTH || this == Direction.SOUTH;
	}

	/**If direction is directly west or east*/
	public boolean isWestEast()
	{
		return this == Direction.WEST || this == Direction.EAST;
	}

	/**If directions are directly opposite, such as {@link #WEST} and {@link #EAST}*/
	public boolean isOpposite(Direction direction)
	{
		return direction == direction.getOpposite();
	}

	/**If direction is cardinal;
	 * <br>Same as {@link Cardinals#equals(Object)}*/
	public boolean isCardinal()
	{
		return Cardinals.equals(this);
	}

	/**If direction is inter-cardinal;
	 * <br>Same as {@link InterCardinals#equals(Object)}*/
	public boolean isInterCardinal()
	{
		return InterCardinals.equals(this);
	}

	/**Gets the next clockwise directional value of passed-in direction*/
	public static Direction getClockwise(Direction direction)
	{
		switch(direction)
		{
			case EAST: return Direction.SOUTHEAST;
			case NORTHEAST: return Direction.EAST;
			case NORTH: return Direction.NORTHEAST;
			case NORTHWEST: return Direction.NORTH;
			case WEST: return Direction.NORTHWEST;
			case SOUTHWEST: return Direction.WEST;
			case SOUTH: return Direction.SOUTHWEST;
			case SOUTHEAST: return Direction.SOUTH;
			default: return Direction.CENTER;
		}
	}

	/**Gets the next counter-clockwise directional value of passed-in direction*/
	public static Direction getCounterClockwise(Direction direction)
	{
		switch(direction)
		{
			case EAST: return Direction.NORTHEAST;
			case NORTHEAST: return Direction.NORTH;
			case NORTH: return Direction.NORTHWEST;
			case NORTHWEST: return Direction.WEST;
			case WEST: return Direction.SOUTHWEST;
			case SOUTHWEST: return Direction.SOUTH;
			case SOUTH: return Direction.SOUTHEAST;
			case SOUTHEAST: return Direction.EAST;
			default: return Direction.CENTER;
		}
	}

	/**Gets the estimated {@link Direction} of passed-in degrees*/
	public static Direction getDirection(double direction)
	{
		direction = (direction + 22.5 + 360) % 360;
		switch ((int) direction / 45)
		{
			case 0: return Direction.EAST;
			case 1: return Direction.NORTHEAST;
			case 2: return Direction.NORTH;
			case 3: return Direction.NORTHWEST;
			case 4: return Direction.WEST;
			case 5: return Direction.SOUTHWEST;
			case 6: return Direction.SOUTH;
			case 7: return Direction.SOUTHEAST;
			default: return Direction.CENTER;
		}
	}
}
