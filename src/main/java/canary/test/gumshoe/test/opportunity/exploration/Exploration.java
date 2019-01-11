package canary.test.gumshoe.test.opportunity.exploration;

import canary.test.gumshoe.test.venue.layout.Entrance;
import canary.test.gumshoe.test.venue.layout.Location;

import canary.bstone.console.Console;
import canary.bstone.console.state.ConsoleState;
import canary.bstone.console.style.ConsoleStyle;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 12/6/17.
 */
public class Exploration implements ConsoleState
{
	protected Explorer explorer;

	public Exploration setExplorer(Explorer explorer)
	{
		this.explorer = explorer;
		return this;
	}

	public void printAvailablePaths(Console con)
	{
		Location currentRoom = this.explorer.getCurrentRoom();
		Set<Entrance> res = new HashSet<>();
		this.explorer.getVenue().getAvailableEntrances(currentRoom, res);

		Location prevRoom = this.explorer.getPreviousRoom();

		for(Entrance entrance : res)
		{
			Location other = entrance.getOtherLocation(currentRoom);
			if (prevRoom != other)
			{
				ConsoleStyle.button(con, other, e -> this.onMoveTo(con, other));
			}
		}
		con.println();

		if (prevRoom != null)
		{
			ConsoleStyle.button(con, "back", e -> this.onMoveBack(con));
		}

		if (currentRoom == this.explorer.getVenue().getOutside())
		{
			ConsoleStyle.button(con, "leave", e -> this.onMoveLeave(con));
		}
	}

	protected void onMoveTo(Console con, Location room)
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
		ConsoleStyle.title(con, "Exploration");
		con.println("You have arrived in the " + this.getExplorer().getCurrentRoom() + ".");
		this.printAvailablePaths(con);
		con.waitForNewActionEvent();
		con.println();
		con.waitForContinue();
		con.states().next(con, this);
	}
}
