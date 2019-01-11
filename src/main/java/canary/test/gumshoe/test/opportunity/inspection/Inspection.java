package canary.test.gumshoe.test.opportunity.inspection;

import canary.test.gumshoe.test.opportunity.exploration.Exploration;
import canary.test.gumshoe.test.venue.layout.Location;

import canary.bstone.console.Console;
import canary.bstone.console.style.ConsoleStyle;

/**
 * Created by Andy on 12/6/17.
 */
public class Inspection extends Exploration
{
	protected Inspector inspector;

	public Inspection setInspector(Inspector inspector)
	{
		this.inspector = inspector;
		return this;
	}

	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, "Inspection");

		Location location = this.getExplorer().getCurrentRoom();

		con.println("You have arrived in the " + location + ".");
		con.println();

		for(Actor actor : this.inspector.getVenue().getActorsByLocation(location))
		{
			con.println("You see " + actor.getName() + ".");
		}
		con.println();

		this.printAvailableActions(con);
		this.printAvailablePaths(con);
		con.waitForNewActionEvent();

		con.println();
		con.sleep(30);

		con.states().next(con, this);
	}

	public void printAvailableActions(Console con)
	{
		Location room = this.getExplorer().getCurrentRoom();
		Iterable<Actor> actors = this.inspector.getVenue().getActorsByLocation(room);
		for(Actor actor : actors)
		{
			ConsoleStyle.button(con, "Talk to " + actor.getName(), e -> onOpenConversation(con, actor));
		}
	}

	public void onOpenConversation(Console con, Actor actor)
	{
		con.println();
		con.println("You talk to " + actor.getName() + ".");

		con.states().next(con, new Conversation(actor));
	}

	public Inspector getInspector()
	{
		return this.inspector;
	}
}
