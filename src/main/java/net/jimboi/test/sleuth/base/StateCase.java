package net.jimboi.test.sleuth.base;

import net.jimboi.test.sleuth.Actor;
import net.jimboi.test.sleuth.ActorFactory;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.RoomType;

import org.bstone.console.Console;
import org.bstone.console.ConsoleUtil;

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
		ConsoleUtil.title(con, "Case");

		if (!DATA.contains("room"))
		{
			ConsoleUtil.message(con, "You arrive at the Crime Scene.");
		}
		else
		{
			ConsoleUtil.message(con, "You are in the " + DATA.get("room").toString());
		}
		ConsoleUtil.newline(con);

		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		ConsoleUtil.button(con, "Look Around", this::lookAround);
		ConsoleUtil.newline(con);
		ConsoleUtil.button(con, "Check Inventory", this::checkInventory);
		ConsoleUtil.newline(con);
		ConsoleUtil.button(con, "Check Notes", this::checkNotes);
		ConsoleUtil.newline(con);
		ConsoleUtil.button(con, "Back", this::back);
		ConsoleUtil.newline(con);

		ConsoleUtil.newline(con);

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
					ConsoleUtil.button(con, "Go to " + other,
							() -> this.moveToRoom(con, other));
				}
				ConsoleUtil.newline(con);
			}
		}
	}

	private void moveToRoom(Console con, Room room)
	{
		DATA.put("room", room);

		con.clear();
		ConsoleUtil.message(con, "BAM! Gone to " + room.type);
		ConsoleUtil.newline(con);
		ConsoleUtil.button(con, "Back", this::restart);
	}

	private void lookAround(Console con)
	{
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		con.setTypeWriterMode(false);
		for(Actor actor : DATA.<Iterable<Actor>>get("actors"))
		{
			ConsoleUtil.message(con, actor.toString());
			ConsoleUtil.newline(con);
		}
		con.setTypeWriterMode(true);

		ConsoleUtil.message(con, "Nothing interesting.");
		ConsoleUtil.newline(con);
	}

	private void checkInventory(Console con)
	{
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		ConsoleUtil.message(con, "Nothing interesting.");
		ConsoleUtil.newline(con);
	}

	private void checkNotes(Console con)
	{
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		ConsoleUtil.message(con, "Nothing interesting.");
		ConsoleUtil.newline(con);
	}
}
