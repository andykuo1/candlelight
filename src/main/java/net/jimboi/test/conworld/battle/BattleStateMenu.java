package net.jimboi.test.conworld.battle;

import net.jimboi.test.conworld.BattleMain;
import net.jimboi.test.conworld.view.ActorSelectorView;
import net.jimboi.test.conworld.view.InventoryView;
import net.jimboi.test.conworld.view.StatView;
import net.jimboi.test.conworld.world.World;

import org.bstone.console.Console;
import org.bstone.console.ConsoleUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/29/17.
 */
public class BattleStateMenu extends BattleState
{
	private final World world;

	private final Set<StatView> statViews = new HashSet<>();
	private final Set<InventoryView> inventoryViews = new HashSet<>();

	public BattleStateMenu(World world)
	{
		this.world = world;
	}

	@Override
	public void initialize()
	{
		final Console console = BattleMain.getConsole();

		ConsoleUtil.title(console, "Battle View");

		ConsoleUtil.button(console, "View Stats", () -> {
			new ActorSelectorView(this.world,
					(actor) -> true,
					(actor) -> {
						for(StatView view : this.statViews)
						{
							if (actor.equals(view.getActor()))
							{
								view.refresh();
								return;
							}
						}

						StatView view = new StatView(actor);
						view.create();
						this.statViews.add(view);
					}).create();
		});

		ConsoleUtil.button(console, "View Inventory", () -> {
			new ActorSelectorView(this.world,
					(actor) -> true,
					(actor) -> {
						for(InventoryView view : this.inventoryViews)
						{
							if (actor.equals(view.getActor()))
							{
								view.refresh();
								return;
							}
						}

						InventoryView view = new InventoryView(actor);
						view.create();
						this.inventoryViews.add(view);
					}).create();
		});

		ConsoleUtil.button(console, "Next Round", this.world::nextTurn);

		ConsoleUtil.button(console, "End Battle", BattleMain::stop);
	}
}
