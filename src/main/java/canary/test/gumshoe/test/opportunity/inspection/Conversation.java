package canary.test.gumshoe.test.opportunity.inspection;

import canary.test.gumshoe.test.opportunity.inspection.memory.DerivedMemory;
import canary.test.gumshoe.test.opportunity.inspection.memory.Memory;
import canary.test.gumshoe.test.opportunity.inspection.memory.MemoryDatabase;
import canary.test.gumshoe.test.opportunity.inspection.memory.SensoryMemory;
import canary.test.gumshoe.test.opportunity.inspection.query.QueryString;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import canary.test.sleuth.data.Time;

import canary.bstone.console.Console;
import canary.bstone.console.state.ConsoleState;
import canary.bstone.console.style.ConsoleStyle;

import java.util.List;

/**
 * Created by Andy on 12/6/17.
 */
public class Conversation implements ConsoleState
{
	private final Actor actor;
	private final MemoryDatabase database;

	public Conversation(Actor actor)
	{
		this.actor = actor;
		this.database = this.actor.getDatabase();
	}

	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, "Conversation - " + this.actor.getName());

		con.println("Hello.");
		con.println("What do you need?");
		con.println();

		ConsoleStyle.button(con, "Talk about...", () -> {
			con.println("There's nothing to talk about.");

			con.println();
			con.waitForContinue();

			con.states().refresh(con);
		});
		ConsoleStyle.button(con, "Ask about...", () -> {
			this.askAbout(con);

			con.states().refresh(con);
		});
		ConsoleStyle.button(con, "Confront about...", () -> {
			con.println("There's nothing to confront about.");

			con.println();
			con.waitForContinue();

			con.states().refresh(con);
		});
		ConsoleStyle.button(con, "Nothing", () -> {
			con.println("Alright then.");
			con.println("Goodbye.");

			con.println();
			con.waitForContinue();

			con.states().back(con);
		});

		con.println();
		con.waitForNewActionEvent();
	}

	public void askAbout(Console con)
	{
		con.println("...what?");

		String s = con.waitForNewResponse(s1 -> !s1.isEmpty());
		List<Memory> memories = null;

		if (s.endsWith(":00"))
		{
			int hour = -1;
			try
			{
				hour = Integer.parseInt(s.substring(0, s.indexOf(':')));
			}
			catch (Exception e) {}

			if (hour >= 0)
			{
				Time time = new Time(hour);
				memories = this.database.query(time);
			}
		}

		if (memories == null)
		{
			QueryString q = new QueryString(s);
			con.println("...\"" + s + "\"...");
			con.println();
			memories = this.database.query(q);
		}

		if (memories.isEmpty())
		{
			con.println("I know nothing about \"" + s + "\".");
		}
		else
		{
			Time time = null;
			int maxMemories = 10;

			for(int i = Math.min(memories.size(), maxMemories) - 1; i >= 0; --i)
			{
				Memory m = memories.get(i);

				if (!m.getTime().equals(time))
				{
					time = m.getTime();
					con.println("At " + time.getHour() + ":00...");
				}

				con.print("  ");
				con.println(Memory.toString(m instanceof SensoryMemory ? ((SensoryMemory) m).getInput() : Sensory.AUTONOMIC, m instanceof DerivedMemory ? ((DerivedMemory) m).getSource() : null, m instanceof SensoryMemory ? ((SensoryMemory) m).getSubject() : null, m.getContent()));
			}

			if (memories.size() > maxMemories)
			{
				con.println();
				con.println("There was something else, but I can't remember.");
			}
		}

		con.println();
		con.waitForContinue();
	}

	public Actor getActor()
	{
		return this.actor;
	}
}
