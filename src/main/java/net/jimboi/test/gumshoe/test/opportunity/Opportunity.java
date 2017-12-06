package net.jimboi.test.gumshoe.test.opportunity;

import net.jimboi.test.gumshoe.test.exploration.Exploration;
import net.jimboi.test.gumshoe.test.exploration.Explorer;
import net.jimboi.test.gumshoe.test.venue.EntranceType;
import net.jimboi.test.gumshoe.test.venue.RoomType;
import net.jimboi.test.gumshoe.test.venue.VenueBuilder;
import net.jimboi.test.gumshoe.test.venue.VenueLayout;

import org.bstone.console.Console;
import org.bstone.console.program.ConsoleProgram;
import org.bstone.console.style.ConsoleStyle;

/**
 * Created by Andy on 12/5/17.
 */
public class Opportunity extends Exploration
{
	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, "Night of Opportunity");

		con.println("You arrive at the gates of the Mansion.");
		con.println();
		con.println("From what you know, the butler called the police earlier this morning at 9:30 AM, informing you of a murder.");
		con.println();
		con.println("The butler explains that his Master was hosting a dinner party for a few guests.");
		con.println("After the dinner, the guests went off to their bedrooms for the night.");
		con.println();
		con.println("Morning came, and the butler found his Master on the floor. Dead.");
		con.println();
		con.println("Everyone is still at the mansion, waiting for the police.");
		con.println();
		con.println("So then, who dun it?");

		next(con, this::setup);
	}

	public void setup(Console con)
	{
		con.println("Building Venue...");
		VenueBuilder vb = new VenueBuilder();
		vb.getRoot().add("livingRoom", EntranceType.DOOR);
		vb.getRoot().add("livingRoom", EntranceType.WINDOW);
		vb.getRoot().add("kitchen", EntranceType.WINDOW);
		vb.get("livingRoom").add("diningRoom", EntranceType.HALL);
		vb.get("diningRoom").add("kitchen", EntranceType.HALL);
		vb.get("livingRoom").add("kitchen", EntranceType.HALL);
		vb.get("livingRoom").add("bathroom", EntranceType.DOOR);

		vb.get("livingRoom").setType(RoomType.LIVING_ROOM);
		vb.get("diningRoom").setType(RoomType.DINING_ROOM);
		vb.get("kitchen").setType(RoomType.KITCHEN);
		vb.get("bathroom").setType(RoomType.BATHROOM);
		VenueLayout venue = vb.bake();

		con.println("Creating explorer...");
		this.setExplorer(new Explorer(venue));

		con.println("Ready!");

		next(con, this::main2);
	}

	public void main2(Console con)
	{
		ConsoleStyle.title(con, "The Mansion");

		this.printAvailablePaths(con);
		con.waitForNewActionEvent();

		next(con, this::main2);
	}

	private static void next(Console con, ConsoleProgram prog)
	{
		con.waitForContinue();
		con.board().next(con, prog);
	}

	private static void quit(Console con)
	{
		con.info("'quit' to continue");
		con.waitForNewResponse("quit"::equals);
		con.stop();
	}

	public static void main(String[] args)
	{
		Console console = new Console("Opportunity");
		ConsoleStyle.setDarkMode(console);
		console.writer().setTypeDelay(0.5);
		console.start(new Opportunity());
		System.exit(0);
	}
}
