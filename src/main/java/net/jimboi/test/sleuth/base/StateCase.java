package net.jimboi.test.sleuth.base;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleUtil;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.RoomType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/7/17.
 */
public class StateCase extends GameState
{
	public StateCase(Game game)
	{
		super(game);

		if (!this.game.contains("venue"))
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
			this.game.put("venue", vb.bake());
		}
	}

	@Override
	protected void onScreenStart(Console con)
	{
		ConsoleUtil.title(con, "Case");

		if (!this.game.contains("room"))
		{
			ConsoleUtil.message(con, "You arrive at the Crime Scene.");
		}
		else
		{
			ConsoleUtil.message(con, "You are in the " + this.game.get("room").toString());
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

		VenueLayout venue = this.game.get("venue");
		Room room = this.game.get("room");

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
		this.game.put("room", room);

		con.clear();
		ConsoleUtil.message(con, "BAM! Gone to " + room.type);
		ConsoleUtil.newline(con);
		ConsoleUtil.button(con, "Back", this::restart);
	}

	private void lookAround(Console con)
	{
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

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
