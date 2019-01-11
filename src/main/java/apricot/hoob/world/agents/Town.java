package apricot.hoob.world.agents;

import apricot.hoob.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/13/17.
 */
public class Town extends StaticAgent
{
	private List<ActionAgent> buttons = new ArrayList<>();
	private boolean menu;

	public Town(World world)
	{
		super(world);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.pos.distanceSquared(this.world.player.pos) < 4F)
		{
			if (!this.menu)
			{
				this.createMenu(this.buttons);
				this.menu = true;
			}
		}
		else
		{
			if (this.menu)
			{
				this.destroyMenu(this.buttons);
				this.menu = false;
			}
		}

		if (this.menu)
		{
			for(ActionAgent agent : this.buttons)
			{
				if (agent.tryActivate(this.world.pickPos.x(), this.world.pickPos.y()))
				{
					break;
				}
			}
		}
	}

	private void createMenu(List<ActionAgent> dst)
	{
		System.out.println("CREAT MEANU");
		ActionAgent agent;
		dst.add(agent = new ActionAgent(this.world, (a) -> {
			System.out.println("BUY");
		}));
		agent.pos.x = this.pos.x + -2;
		agent.pos.y = this.pos.y + 2;
		dst.add(agent = new ActionAgent(this.world, (a) -> {
			System.out.println("SELL");
		}));
		agent.pos.x = this.pos.x + 0;
		agent.pos.y = this.pos.y + 2;
		dst.add(agent = new ActionAgent(this.world, (a) -> {
			System.out.println("HIRE");
		}));
		agent.pos.x = this.pos.x + 2;
		agent.pos.y = this.pos.y + 2;

		for(WorldAgent a : dst)
		{
			this.world.spawnAgent(a);
		}
	}

	private void destroyMenu(List<ActionAgent> dst)
	{
		System.out.println("DESTR MEANU");
		for(WorldAgent agent : dst)
		{
			this.world.destroyAgent(agent);
		}
		dst.clear();
	}
}
