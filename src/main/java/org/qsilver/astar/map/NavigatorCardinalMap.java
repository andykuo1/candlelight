package org.qsilver.astar.map;

import org.qsilver.astar.AstarNavigator;
import org.qsilver.astar.HeuristicMap;
import org.qsilver.astar.HeuristicMapMaker;
import org.qsilver.astar.NavigatorMap;

public class NavigatorCardinalMap extends NavigatorMap<NavigatorCardinalMap.Cell>
{
	/** Map of pass/solid cells; If true then cell is SOLID */
	protected final boolean[] map;
	protected final int width;
	protected final int height;

	protected HeuristicMapMaker<Cell> mapMaker;

	public NavigatorCardinalMap(boolean[] map, int width, int height)
	{
		this.map = map;
		this.width = width;
		this.height = height;

		this.mapMaker = new HeuristicMapMaker<>((m, from, to) -> (from.x - to.x) * (from.x - to.x) + (from.y - to.y) * (from.y - to.y));
	}

	/** Update map and calculations to passed-in map */
	public void setMap(boolean[] map)
	{
		assert (map.length == this.map.length);

		for (int i = 0; i < this.map.length; ++i)
		{
			this.map[i] = map[i];
		}

		this.mapMaker.clearCache();
	}

	@Override
	protected int[] getNodeNeighbors(int index)
	{
		Cell cell = new Cell(index % this.width, index / this.width);

		assert (this.isValid(cell.x, cell.y));

		Cell c0 = new Cell(cell.x - 1, cell.y);
		Cell c1 = new Cell(cell.x, cell.y - 1);
		Cell c2 = new Cell(cell.x + 1, cell.y);
		Cell c3 = new Cell(cell.x, cell.y + 1);

		int[] result = new int[4];
		result[0] = this.isValid(c0.x, c0.y) && !this.map[c0.toIndex()] ? c0.toIndex() : -1;
		result[1] = this.isValid(c1.x, c1.y) && !this.map[c1.toIndex()] ? c1.toIndex() : -1;
		result[2] = this.isValid(c2.x, c2.y) && !this.map[c2.toIndex()] ? c2.toIndex() : -1;
		result[3] = this.isValid(c3.x, c3.y) && !this.map[c3.toIndex()] ? c3.toIndex() : -1;
		return result;
	}

	@Override
	protected HeuristicMap<Cell> getNodeHeuristicMap(int to)
	{
		return this.mapMaker.createMap(this, to);
	}

	@Override
	protected int getNodeIndexOf(Cell node)
	{
		return node.toIndex();
	}

	@Override
	protected Cell getNodeObjectOf(int index)
	{
		return new Cell(index % this.width, index / this.width);
	}

	public boolean isValid(int x, int y)
	{
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	public AstarNavigator<Cell> createNavigator()
	{
		return new AstarNavigator<>(this, (map, from, to) -> 10);
	}

	public int width()
	{
		return this.width;
	}

	public int height()
	{
		return this.height;
	}

	@Override
	protected int size()
	{
		return this.width * this.height;
	}

	public class Cell
	{
		public final int x;
		public final int y;

		public Cell(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public int toIndex()
		{
			return this.x + this.y * NavigatorCardinalMap.this.width;
		}

		public String toString()
		{
			return "[" + this.x + "," + this.y + "]";
		}

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof Cell)
			{
				return this.x == ((Cell) o).x && this.y == ((Cell) o).y;
			}

			return super.equals(o);
		}
	}
}
