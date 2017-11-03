package net.jimboi.canary.stage_a.lantern.newglim.entity.component;

import net.jimboi.apricot.base.astar.AstarNavigator;
import net.jimboi.apricot.base.astar.map.NavigatorCardinalMap;
import net.jimboi.canary.stage_a.lantern.newglim.WorldGlim;

import org.zilar.entity.EntityComponent;

import java.util.Stack;

/**
 * Created by Andy on 6/14/17.
 */
public class EntityComponentNavigator implements EntityComponent
{
	public WorldGlim world;
	public final AstarNavigator<NavigatorCardinalMap.Cell> navigator;
	public Stack<NavigatorCardinalMap.Cell> tracks;

	public EntityComponentNavigator(WorldGlim world, AstarNavigator<NavigatorCardinalMap.Cell> navigator)
	{
		this.world = world;
		this.navigator = navigator;
	}
}
