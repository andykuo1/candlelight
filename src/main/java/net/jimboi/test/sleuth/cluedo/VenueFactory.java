package net.jimboi.test.sleuth.cluedo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 9/24/17.
 */
public class VenueFactory
{
	public static Venue create(Random rand, int capacity)
	{
		final Venue venue = new Venue();

		//This is not a studio or an apartment.

		List<RoomType> rooms = new ArrayList<>();

		//MASTER BATH
		rooms.add(RoomType.MASTER_BEDROOM);
		if (rand.nextInt(10) < 8) rooms.add(RoomType.BATHROOM);
		if (rand.nextInt(10) < 8) rooms.add(RoomType.CLOSET);

		//GUEST BATH
		rooms.add(RoomType.BATHROOM);

		//IS IT A MANSION?
		boolean mansion = capacity >= 8;
		boolean large = capacity >= 4;
		boolean medium = capacity >= 3;

		//1 - 6
		//6+
		for(int i = 2; i < capacity; ++i)
		{
			rooms.add(RoomType.BEDROOM);
			if (mansion || rand.nextInt(10) < capacity) rooms.add(RoomType.BATHROOM);
			rooms.add(RoomType.CLOSET);
		}

		//KITCHEN
		rooms.add(RoomType.KITCHEN);
		if (large || rand.nextBoolean()) rooms.add(RoomType.PANTRY);

		//LIVING ROOM
		rooms.add(RoomType.LIVING_ROOM);
		if (large || rand.nextBoolean()) rooms.add(RoomType.CLOSET);

		//DINING ROOM
		if (large || rand.nextInt(10) < 3) rooms.add(RoomType.DINING_ROOM);

		//GARAGE
		if (rand.nextInt(10) < 9) rooms.add(RoomType.GARAGE);

		//OUTSIDE
		rooms.add(RoomType.FRONT_YARD);
		rooms.add(RoomType.BACKYARD);
		if (rand.nextInt(10) < 3) rooms.add(RoomType.PATIO);

		//HALLWAY
		if (medium || rand.nextInt(10) < 8) rooms.add(RoomType.HALLWAY);

		//SECOND FLOOR
		boolean secondFloor = rand.nextInt(10) < 3;
		if (secondFloor)
		{
			rooms.add(RoomType.STAIRCASE);
			rooms.add(RoomType.HALLWAY);
			if (rand.nextInt(10) < 1) rooms.add(RoomType.STAIRCASE);
			if (large && rand.nextInt(10) < 1) rooms.add(RoomType.HALLWAY);

			//BALCONY
			if (rand.nextInt(10) < 3) rooms.add(RoomType.BALCONY);
		}

		//ATTIC
		if (rand.nextInt(10) < 7) rooms.add(RoomType.ATTIC);

		//BASEMENT
		boolean hasTornado = false;
		boolean basement = hasTornado && rand.nextInt(10) < 8 || rand.nextInt(10) < 1;
		if (basement)
		{
			rooms.add(RoomType.BASEMENT);

			if (medium && rand.nextInt(10) < 4) rooms.add(RoomType.CELLAR);
		}

		//LAUNDRY
		if (large ? rand.nextInt(10) < 8 : rand.nextInt(10) < 5)
		{
			rooms.add(RoomType.LAUNDRY);
		}

		//FOYER
		if (rand.nextInt(10) < 8) rooms.add(RoomType.FOYER);

		int misc = 3 - rand.nextInt(capacity);

		//OFFICE
		if (misc > 0 && rand.nextInt(10) < 3)
		{
			rooms.add(RoomType.OFFICE);
			misc++;
		}

		//DEN
		if (misc > 0 && rand.nextInt(10) < 4)
		{
			rooms.add(RoomType.DEN);
			misc++;
		}

		//STUDY ROOM
		if (misc > 0 && rand.nextInt(10) < 3)
		{
			rooms.add(RoomType.STUDY_ROOM);
			misc++;
		}

		//SUN ROOM
		if (misc > 0 && large && rand.nextInt(10) < 1)
		{
			rooms.add(RoomType.SUN_ROOM);
			misc++;
		}

		//ENTERTAINMENT ROOM
		if (misc > 0 && large && rand.nextInt(10) < 2)
		{
			rooms.add(RoomType.ENTERTAINMENT_ROOM);
			misc++;
		}

		//FAMILY ROOM
		if (misc > 0 && large && rand.nextInt(10) < 2)
		{
			rooms.add(RoomType.FAMILY_ROOM);
			misc++;
		}

		//WORKSHOP
		if (misc > 0 && rand.nextInt(10) < 1)
		{
			rooms.add(RoomType.WORKSHOP);
			misc++;
		}

		//Create rooms
		List<Room> newRooms = new ArrayList<>();
		for(RoomType type : rooms)
		{
			newRooms.add(new Room(type));
		}
		venue.rooms = newRooms;
		return venue;
	}
}
