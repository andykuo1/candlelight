package net.jimboi.test.gumshoe.test.exploration;

import net.jimboi.test.gumshoe.test.venue.Entrance;
import net.jimboi.test.gumshoe.test.venue.Room;

import org.bstone.console.Console;
import org.bstone.console.program.ConsoleProgram;
import org.bstone.console.style.ConsoleStyle;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 12/6/17.
 */
public class Exploration implements ConsoleProgram
{
	protected Explorer explorer;

	public Exploration setExplorer(Explorer explorer)
	{
		this.explorer = explorer;
		return this;
	}

	public void printAvailablePaths(Console con)
	{
		Room currentRoom = this.explorer.getCurrentRoom();
		con.println("You have arrived in the " + currentRoom + ".");

		Set<Entrance> res = new HashSet<>();
		this.explorer.getVenue().getAvailableEntrances(currentRoom, res);

		for(Entrance entrance : res)
		{
			con.print(" > ");
			Room other = entrance.getOtherRoom(currentRoom);
			con.link(other, e -> this.onMoveTo(con, other));
			con.println();
		}

		con.println();

		if (this.explorer.canMoveBack())
		{
			con.print(" > ");
			con.link("back", e -> this.onMoveBack(con));
			con.println();
		}
		else
		{
			con.print(" > ");
			con.link("leave", e -> this.onMoveLeave(con));
			con.println();
		}
	}

	protected void onMoveTo(Console con, Room room)
	{
		this.explorer.moveTo(room);

		con.println();
		con.println("You enter the " + this.explorer.getCurrentRoom() + "...");
	}

	protected void onMoveBack(Console con)
	{
		this.explorer.moveBack();

		con.println();
		con.println("You go back to the " + this.explorer.getCurrentRoom() + "...");
	}

	protected void onMoveLeave(Console con)
	{
		con.stop();
		con.println();
	}

	public Explorer getExplorer()
	{
		return this.explorer;
	}

	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, "Venue");
		this.printAvailablePaths(con);
		con.waitForNewActionEvent();
		con.println();
		con.waitForContinue();
		con.board().next(con, this);
	}
}
