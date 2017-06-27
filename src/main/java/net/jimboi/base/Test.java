package net.jimboi.base;

import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.BoundingManager;

import org.joml.Vector3fc;
import org.qsilver.astar.AstarNavigator;
import org.qsilver.astar.map.NavigatorCardinalMap;
import org.qsilver.poma.Poma;

import java.util.Stack;

/**
 * Created by Andy on 5/28/17.
 */
public class Test
{
	public static void main(String[] args)
	{
		Poma.makeSystemLogger();

		BoundingManager boundingManager = new BoundingManager();
		WorldGlim worldGlim = new WorldGlim(boundingManager);
		Vector3fc start = worldGlim.getRandomTilePos(false);
		Vector3fc end = worldGlim.getRandomTilePos(false);

		System.out.println("WORLD MAP!");
		for(int x = 0; x < worldGlim.getMap().width; ++x)
		{
			for(int y = 0; y < worldGlim.getMap().height; ++y)
			{
				if (x == (int)start.x() && y == (int)start.z())
				{
					System.out.print("@ ");
					continue;
				}
				if (x == (int)end.x() && y == (int)end.z())
				{
					System.out.print("X ");
					continue;
				}

				int i = worldGlim.getMap().get(x, y);
				if (i == 0)
				{
					System.out.print(' ');
				}
				else
				{
					System.out.print('_');
				}
				System.out.print(' ');
			}
			System.out.println();
		}

		System.out.println("NAVIGATION MAP!");
		NavigatorCardinalMap navigatorMap = new NavigatorCardinalMap(worldGlim.getSolids(), worldGlim.getMap().width, worldGlim.getMap().height);
		AstarNavigator<NavigatorCardinalMap.Cell> navigator = new AstarNavigator<>(navigatorMap);
		NavigatorCardinalMap.Cell startcell = new NavigatorCardinalMap.Cell((int)start.x(), (int)start.z());
		NavigatorCardinalMap.Cell endcell = new NavigatorCardinalMap.Cell((int)end.x(), (int)end.z());

		Stack<NavigatorCardinalMap.Cell> stack = navigator.navigate(startcell, endcell);

		for(int x = 0; x < worldGlim.getMap().width; ++x)
		{
			for(int y = 0; y < worldGlim.getMap().height; ++y)
			{
				boolean flag = false;

				if (x == startcell.x && y == startcell.y)
				{
					System.out.print("@ ");
					flag = true;
				}
				if (!flag)
				{
					if (x == endcell.x && y == endcell.y)
					{
						System.out.print("X ");
						flag = true;
					}
				}
				if (!flag)
				{
					for (NavigatorCardinalMap.Cell cell : stack)
					{
						if (x == cell.x && y == cell.y)
						{
							System.out.print("#");
							flag = true;
							break;
						}
					}
				}
				if (!flag)
				{
					int i = worldGlim.getMap().get(x, y);
					if (i == 0)
					{
						System.out.print(' ');
					}
					else
					{
						System.out.print('_');
					}
				}
				System.out.print(' ');
			}
			System.out.println();
		}
	}
}
