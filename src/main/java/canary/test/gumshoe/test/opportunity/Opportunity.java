package canary.test.gumshoe.test.opportunity;

import canary.test.gumshoe.test.opportunity.exploration.Exploration;
import canary.test.gumshoe.test.opportunity.exploration.Explorer;
import canary.test.gumshoe.test.opportunity.inspection.Inspection;
import canary.test.gumshoe.test.opportunity.inspection.Inspector;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Simulation;
import canary.test.gumshoe.test.venue.layout.EntranceType;
import canary.test.gumshoe.test.venue.layout.VenueBuilder;
import canary.test.gumshoe.test.venue.layout.VenueLayout;

import canary.bstone.console.Console;
import canary.bstone.console.state.ConsoleState;
import canary.bstone.console.style.ConsoleStyle;

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
		vb.getRoot().add("Foyer", EntranceType.DOOR);
		vb.get("Foyer").add("Hallway", EntranceType.HALL);
		vb.get("Foyer").add("Lounge", EntranceType.HALL);
		vb.get("Hallway").add("DiningRoom", EntranceType.HALL);
		vb.get("Hallway").add("Kitchen", EntranceType.DOOR);
		vb.get("Hallway").add("StudyRoom", EntranceType.DOOR);
		vb.get("Hallway").add("Bathroom", EntranceType.DOOR);
		vb.get("StudyRoom").add("Conservatory", EntranceType.DOOR);
		vb.get("StudyRoom").add("Library", EntranceType.DOOR);
		vb.get("Lounge").add("BilliardRoom", EntranceType.DOOR);
		vb.get("Lounge").add("DiningRoom", EntranceType.DOOR);
		vb.get("Kitchen").add("Kitchen", EntranceType.DOOR);

		con.println("...decorating rooms...");
		vb.get("Foyer");
		vb.get("Hallway");
		vb.get("Kitchen");
		vb.get("StudyRoom");
		vb.get("DiningRoom");
		vb.get("Library");
		vb.get("Lounge");
		vb.get("Conservatory");
		vb.get("BilliardRoom");
		vb.get("Bathroom");

		VenueLayout venue = vb.bake();

		ConsoleState program = new Inspection();//new Exploration();

		con.println("Creating explorer...");
		Exploration exploration = (Exploration) program;
		exploration.setExplorer(new Explorer(venue));

		con.println("Creating inspector...");
		Inspection inspection = (Inspection) program;
		inspection.setInspector(new Inspector(venue));

		con.println("Simulating...");
		Simulation simulation = new Simulation(inspection.getInspector().getVenue());
		simulation.run();

		con.println("Ready!");
		next(con, program);
	}

	public void main2(Console con)
	{
		ConsoleStyle.title(con, "The Mansion");

		con.println("You have arrived in the " + this.getExplorer().getCurrentRoom() + ".");
		this.printAvailablePaths(con);
		con.waitForNewActionEvent();

		next(con, this::main2);
	}

	private static void next(Console con, ConsoleState prog)
	{
		con.waitForContinue();
		con.states().next(con, prog);
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
		console.writer().setTypeDelay(0.1);
		console.start(new Opportunity());
		System.exit(0);
	}
}
