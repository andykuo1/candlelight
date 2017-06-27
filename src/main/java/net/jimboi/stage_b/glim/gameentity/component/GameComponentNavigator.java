package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.glim.WorldGlim;

import org.qsilver.astar.AstarNavigator;
import org.qsilver.astar.map.NavigatorCardinalMap;

import java.util.Stack;

/**
 * Created by Andy on 6/14/17.
 */
public class GameComponentNavigator extends GameComponent
{
	public WorldGlim world;
	public final AstarNavigator<NavigatorCardinalMap.Cell> navigator;
	public Stack<NavigatorCardinalMap.Cell> tracks;

	public GameComponentNavigator(WorldGlim world, AstarNavigator<NavigatorCardinalMap.Cell> navigator)
	{
		this.world = world;
		this.navigator = navigator;
	}
}
