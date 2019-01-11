package canary.test.conworld.view;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.world.World;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Andy on 8/30/17.
 */
public class ActorSelectorView extends View
{
	protected final World world;
	protected final Predicate<Actor> filter;
	protected final Consumer<Actor> callback;

	public ActorSelectorView(World world, Predicate<Actor> filter, Consumer<Actor> callback)
	{
		super(new Console(320, 480));

		this.world = world;
		this.filter = filter;
		this.callback = callback;
	}

	@Override
	protected void initialize(Console console)
	{
		ConsoleStyle.title(console, "Select Actor");

		for(Actor actor : this.world.actors)
		{
			if (this.filter.test(actor))
			{
				ConsoleStyle.button(console, actor.getName(), () ->
				{
					this.callback.accept(actor);
					this.destroy();
				});
			}
		}
	}

	@Override
	protected void terminate(Console console)
	{

	}

	public final World getWorld()
	{
		return this.world;
	}
}
