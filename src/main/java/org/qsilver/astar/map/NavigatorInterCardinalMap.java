package org.qsilver.astar.map;

import org.qsilver.astar.AstarNavigator;

public class NavigatorInterCardinalMap extends NavigatorCardinalMap
{
	public NavigatorInterCardinalMap(boolean[] map, int width, int height)
	{
		super(map, width, height);
	}

	@Override
	protected int[] getNodeNeighbors(int index)
	{
		Cell cell = new Cell(index % this.width, index / this.width);

		assert (this.isValid(cell.x, cell.y));

		Cell c0 = new Cell(cell.x - 1, cell.y + 1);
		Cell c1 = new Cell(cell.x + 1, cell.y - 1);
		Cell c2 = new Cell(cell.x + 1, cell.y + 1);
		Cell c3 = new Cell(cell.x - 1, cell.y - 1);

		int[] cardinal = super.getNodeNeighbors(index);
		int[] result = new int[8];
		result[0] = cardinal[0];
		result[1] = cardinal[1];
		result[2] = cardinal[2];
		result[3] = cardinal[3];
		result[4] = this.isValid(c0.x, c0.y) && !this.map[this.getNodeIndexOf(c0)] ? this.getNodeIndexOf(c0) : -1;
		result[5] = this.isValid(c1.x, c1.y) && !this.map[this.getNodeIndexOf(c1)] ? this.getNodeIndexOf(c1) : -1;
		result[6] = this.isValid(c2.x, c2.y) && !this.map[this.getNodeIndexOf(c2)] ? this.getNodeIndexOf(c2) : -1;
		result[7] = this.isValid(c3.x, c3.y) && !this.map[this.getNodeIndexOf(c3)] ? this.getNodeIndexOf(c3) : -1;
		return result;
	}

	public AstarNavigator<Cell> createNavigator()
	{
		return new AstarNavigator<>(this, (map, from, to) -> (from.x - to.x != 0 && from.y - from.y != 0 ? 14 : 10));
	}
}
