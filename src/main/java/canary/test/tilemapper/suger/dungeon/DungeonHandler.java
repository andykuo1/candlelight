package canary.test.tilemapper.suger.dungeon;

import canary.test.tilemapper.suger.baron.ViewPort;
import canary.test.tilemapper.suger.canvas.LayeredCanvas;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;

/**
 * Created by Andy on 11/15/17.
 */
public class DungeonHandler
{
	private final Dungeon dungeon;

	public DungeonHandler(Dungeon dungeon)
	{
		this.dungeon = dungeon;
	}

	private final Vector2f v1 = new Vector2f();
	private final Vector2f v2 = new Vector2f();

	public Rectangle2D getExportArea(ViewPort view)
	{
		Vector2fc pos = ViewPort.getWorldPos(view,
				0, 0, new Vector2f());
		Vector2dc offset = ViewPort.getScreenPos(view,
				(int) Math.ceil(pos.x()),
				(int) Math.ceil(pos.y()),
				new Vector2d());

		Vector2fc endPos = ViewPort.getWorldPos(view,
				view.getWidth(),
				view.getHeight(), new Vector2f());
		Vector2dc endScreen = ViewPort.getScreenPos(view,
				(int) Math.floor(endPos.x()),
				(int) Math.floor(endPos.y()),
				new Vector2d());

		return new Rectangle2D(offset.x(), offset.y(),
				endScreen.x() - offset.x(), endScreen.y() - offset.y());
	}

	public WritableImage exportToImage(LayeredCanvas canvas, ViewPort view)
	{
		return canvas.toWritableImage(this.getExportArea(view));
	}

	public void solveDirectionsByRegion()
	{
		final int size = this.dungeon.getMap().getChunkSize();

		for(DungeonChunk chunk : this.dungeon.getMap().getLoadedChunks())
		{
			Vector2fc chunkPos = chunk.getChunkPos(this.v1);

			for (int i = 0; i < size; ++i)
			{
				for (int j = 0; j < size; ++j)
				{
					float posX = chunkPos.x() + i;
					float posY = chunkPos.y() + j;

					int region = this.dungeon.getRegion(posX, posY);

					this.dungeon.setDirection(posX, posY, (byte) 0);

					if (this.dungeon.getRegion(posX + 1, posY) == region)
					{
						this.dungeon.addDirection(posX, posY, (byte) 1);
					}

					if (this.dungeon.getRegion(posX, posY - 1) == region)
					{
						this.dungeon.addDirection(posX, posY, (byte) 2);
					}

					if (this.dungeon.getRegion(posX - 1, posY) == region)
					{
						this.dungeon.addDirection(posX, posY, (byte) 4);
					}

					if (this.dungeon.getRegion(posX, posY + 1) == region)
					{
						this.dungeon.addDirection(posX, posY, (byte) 8);
					}
				}
			}
		}
	}

	public void solveDirectionsBySolid(boolean solid)
	{
		final int size = this.dungeon.getMap().getChunkSize();

		for(DungeonChunk chunk : this.dungeon.getMap().getLoadedChunks())
		{
			Vector2fc chunkPos = chunk.getChunkPos(this.v1);

			for (int i = 0; i < size; ++i)
			{
				for (int j = 0; j < size; ++j)
				{
					float posX = chunkPos.x() + i;
					float posY = chunkPos.y() + j;

					if (this.dungeon.isSolid(posX, posY) == solid)
					{
						this.dungeon.setDirection(posX, posY, (byte) 0);

						if (this.dungeon.isSolid(posX + 1, posY) == solid)
						{
							this.dungeon.addDirection(posX, posY, (byte) 1);
						}

						if (this.dungeon.isSolid(posX, posY - 1) == solid)
						{
							this.dungeon.addDirection(posX, posY, (byte) 2);
						}

						if (this.dungeon.isSolid(posX - 1, posY) == solid)
						{
							this.dungeon.addDirection(posX, posY, (byte) 4);
						}

						if (this.dungeon.isSolid(posX, posY + 1) == solid)
						{
							this.dungeon.addDirection(posX, posY, (byte) 8);
						}
					}
				}
			}
		}
	}

	public void solveRegions()
	{
		final int size = this.dungeon.getMap().getChunkSize();
		int nextAvailableRegion = 1;

		for (DungeonChunk chunk : this.dungeon.getMap().getLoadedChunks())
		{
			chunk.regions.clear(0);
		}

		for (DungeonChunk chunk : this.dungeon.getMap().getLoadedChunks())
		{
			Vector2fc chunkPos = chunk.getChunkPos(this.v1);

			for (int i = 0; i < size; ++i)
			{
				for (int j = 0; j < size; ++j)
				{
					float posX = chunkPos.x() + i;
					float posY = chunkPos.y() + j;

					if (!this.dungeon.isSolid(posX, posY))
					{
						int region = this.dungeon.getRegion(posX, posY);
						if (region == 0)
						{
							this.solveSubRegion(posX, posY, nextAvailableRegion++);
						}
					}
				}
			}
		}
	}

	public void solveSubRegion(float posX, float posY, int region)
	{
		Queue<Vector2fc> opened = new LinkedList<>();
		opened.add(new Vector2f(posX, posY));
		Set<Vector2fc> closed = new HashSet<>();

		while (!opened.isEmpty())
		{
			Vector2fc vec = opened.poll();
			closed.add(vec);
			if (!this.dungeon.isChunkLoaded(vec.x(), vec.y())) continue;
			this.dungeon.setRegion(vec.x(), vec.y(), region);

			this.solveSubRegionNeighbor(vec.add(0, 1, this.v2), opened, closed, region);
			this.solveSubRegionNeighbor(vec.add(0, -1, this.v2), opened, closed, region);
			this.solveSubRegionNeighbor(vec.add(1, 0, this.v2), opened, closed, region);
			this.solveSubRegionNeighbor(vec.add(-1, 0, this.v2), opened, closed, region);
		}
	}

	private void solveSubRegionNeighbor(Vector2fc pos, Queue<Vector2fc> opened, Set<Vector2fc> closed, int region)
	{
		if (!closed.contains(pos) && !this.dungeon.isSolid(pos.x(), pos.y()))
		{
			if (!this.dungeon.isPermeable(pos.x(), pos.y()))
			{
				opened.offer(new Vector2f(pos));
			}
			else
			{
				this.dungeon.setRegion(pos.x(), pos.y(), region);
			}
		}
	}
}

