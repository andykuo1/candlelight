package canary.test.conworld.battle;

import canary.test.conworld.BattleMain;
import canary.test.conworld.view.ActorSelectorView;
import canary.test.conworld.view.InventoryView;
import canary.test.conworld.view.StatView;
import canary.test.conworld.world.World;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

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

		ConsoleStyle.title(console, "Battle View");

		ConsoleStyle.button(console, "View Stats", () -> {
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

		ConsoleStyle.button(console, "View Inventory", () -> {
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

		ConsoleStyle.button(console, "Next Round", this.world::nextTurn);

		ConsoleStyle.button(console, "End Battle", BattleMain::stop);
	}
}
