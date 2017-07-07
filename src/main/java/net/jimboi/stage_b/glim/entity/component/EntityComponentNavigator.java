package net.jimboi.stage_b.glim.entity.component;

import net.jimboi.stage_b.glim.WorldGlim;

import org.qsilver.astar.AstarNavigator;
import org.qsilver.astar.map.NavigatorCardinalMap;
import org.qsilver.entity.EntityComponent;

import java.util.Stack;

/**
 * Created by Andy on 6/14/17.
 */
public class EntityComponentNavigator extends EntityComponent
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
