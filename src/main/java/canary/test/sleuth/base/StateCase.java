package canary.test.sleuth.base;

import canary.test.sleuth.Actor;
import canary.test.sleuth.ActorFactory;
import canary.test.sleuth.cluedo.Room;
import canary.test.sleuth.cluedo.RoomType;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 10/7/17.
 */
public class StateCase extends GameState
{
	public StateCase()
	{
		if (!DATA.contains("venue"))
		{
			VenueBuilder vb = new VenueBuilder()
					.createRoom(RoomType.FRONT_YARD, 0).pack()
					.createRoom(RoomType.LIVING_ROOM, 1).pack()
					.createRoom(RoomType.BEDROOM, 2).pack()
					.createRoom(RoomType.BATHROOM, 3).pack()
					.createRoom(RoomType.KITCHEN, 4).pack()
					.createEntrance(EntranceType.DOOR, 0, 1).pack()
					.createEntrance(EntranceType.DOOR, 1, 2).pack()
					.createEntrance(EntranceType.DOOR, 1, 3).pack()
					.createEntrance(EntranceType.HALL, 1, 4).pack()
					.createEntrance(EntranceType.WINDOW, 0, 2).pack()
					.createEntrance(EntranceType.WINDOW, 0, 1).pack()
					.createEntryPoint(0);
			DATA.put("venue", vb.bake());
		}

		if (!DATA.contains("actors"))
		{
			Random rand = new Random();
			List<Actor> actors = new ArrayList<>();
			actors.add(ActorFactory.create(rand, false));
			actors.add(ActorFactory.create(rand, true));
			for(int i = 0; i < 4; ++i)
			{
				actors.add(ActorFactory.create(rand, false));
			}
			DATA.put("actors", actors);
		}
	}

	@Override
	protected void onScreenStart(Console con)
	{
		ConsoleStyle.title(con, "Case");

		if (!DATA.contains("room"))
		{
			ConsoleStyle.message(con, "You arrive at the Crime Scene.");
		}
		else
		{
			ConsoleStyle.message(con, "You are in the " + DATA.get("room").toString());
		}
		ConsoleStyle.newline(con);

		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		ConsoleStyle.button(con, "Look Around", this::lookAround);
		ConsoleStyle.newline(con);
		ConsoleStyle.button(con, "Check Inventory", this::checkInventory);
		ConsoleStyle.newline(con);
		ConsoleStyle.button(con, "Check Notes", this::checkNotes);
		ConsoleStyle.newline(con);
		ConsoleStyle.button(con, "Back", this::back);
		ConsoleStyle.newline(con);

		ConsoleStyle.newline(con);

		VenueLayout venue = DATA.get("venue");
		Room room = DATA.get("room");

		List<Entrance> entrances = venue.getAvailableEntrances(room, new ArrayList<>());
		for(Entrance entrance : entrances)
		{
			EntranceType type = entrance.getType();
			if (type == EntranceType.OUTSIDE || type == EntranceType.DOOR || type == EntranceType.HALL)
			{
				Room other = entrance.getOtherRoom(room);
				if (other == null)
				{
					continue;
				}
				else
				{
					ConsoleStyle.button(con, "Go to " + other,
							() -> this.moveToRoom(con, other));
				}
				ConsoleStyle.newline(con);
			}
		}
	}

	private void moveToRoom(Console con, Room room)
	{
		DATA.put("room", room);

		con.clear();
		ConsoleStyle.message(con, "BAM! Gone to " + room.type);
		ConsoleStyle.newline(con);
		ConsoleStyle.button(con, "Back", this::restart);
	}

	private void lookAround(Console con)
	{
		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		con.setTypeWriterMode(false);
		for(Actor actor : DATA.<Iterable<Actor>>get("actors"))
		{
			ConsoleStyle.message(con, actor.toString());
			ConsoleStyle.newline(con);
		}
		con.setTypeWriterMode(true);

		ConsoleStyle.message(con, "Nothing interesting.");
		ConsoleStyle.newline(con);
	}

	private void checkInventory(Console con)
	{
		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		ConsoleStyle.message(con, "Nothing interesting.");
		ConsoleStyle.newline(con);
	}

	private void checkNotes(Console con)
	{
		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		ConsoleStyle.message(con, "Nothing interesting.");
		ConsoleStyle.newline(con);
	}
}
