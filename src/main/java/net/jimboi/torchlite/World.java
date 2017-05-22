package net.jimboi.torchlite;

import net.jimboi.mod.meshbuilder.MeshBuilder;
import net.jimboi.mod.meshbuilder.MeshData;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class World
{
	private Random rand;
	private int[] map;
	private int size;

	public World(Random rand, int size)
	{
		this.rand = rand;
		this.size = size;
	}

	public void generateWorld()
	{
		final WorldGenBuilder wb = new WorldGenBuilder();
		wb.create(this.rand, this.size, this.size, 15);
		wb.fill(0);
		wb.putRandomSpawnRoom(1, 2, 1, 4);
		wb.putRandomSpawnRoom(1, 3, 1, 4);
		wb.putRandomRooms(1, 100, 1, 4);

		Predicate<Integer> isCarveableTile = wb.getIsTilePredicate(0);
		Supplier<Integer> getConnectorTiles = new Supplier<Integer>(){
			@Override
			public Integer get()
			{
				if (wb.getRandom().nextInt(4) == 0)
				{
					return wb.getRandom().nextInt(3) == 1 ? 9: 1;
				}
				else
				{
					return 8;
				}
			}
		};
		wb.generateMaze(isCarveableTile, 1, 0.75F);
		wb.makeRegionMerges(isCarveableTile, getConnectorTiles, 0.08F);
		wb.makeMazeSparse(0);

		WorldGenData world = wb.bakeAndClear();
		this.map = world.map.toIntArray();
	}

	public int getTile(Vector3fc pos)
	{
		return this.getTile((int)pos.x(), (int)pos.z());
	}

	public int getTile(int x, int y)
	{
		return this.isValid(x, y) ? this.map[x + y * this.size] : 0;
	}

	public boolean isValid(int x, int y)
	{
		return x >= 0 && x < this.size && y >= 0 && y < this.size;
	}

	public int[] getMap()
	{
		return this.map;
	}

	public int getSize()
	{
		return this.size;
	}

	public static MeshData toMeshBoxData(World world, int texWidth, int texHeight)
	{
		MeshBuilder modelBuilder = new MeshBuilder();

		int[] map = world.getMap();
		int size = world.getSize();

		for(int i = 0; i < map.length; ++i)
		{
			Vector3fc wallPos = new Vector3f(i % size, 0, i / size);

			if (map[i] != 0)
			{
				int tile = map[i];

				int textureWidth = texWidth;
				int textureHeight = texHeight;

				int spriteWidth = 16;
				int spriteHeight = 16;

				float textureSpriteWidth = spriteWidth / (float) textureWidth;
				float textureSpriteHeight = spriteHeight / (float) textureHeight;
				Vector2fc sprite = new Vector2f(textureSpriteWidth, textureSpriteHeight);

				//WALL SPRITE
				int wallSpriteX = (tile % (textureWidth / spriteWidth));
				int wallSpriteY = (tile / (textureWidth / spriteWidth)) + 1;
				Vector2fc tc0 = new Vector2f(sprite.x() * wallSpriteX, sprite.y() * wallSpriteY);
				Vector2fc tc1 = tc0.add(sprite, new Vector2f());

				//FLOOR SPRITE
				int index = 0;
				if (i + 1 < map.length && map[i + 1] == tile) //EAST WALL
				{
					index += 1;
				}
				if (i - size >= 0 && map[i - size] == tile) //NORTH WALL
				{
					index += 2;
				}
				if (i - 1 >= 0 && map[i - 1] == tile) //WEST WALL
				{
					index += 4;
				}
				if (i + size < map.length && map[i + size] == tile) //SOUTH WALL
				{
					index += 8;
				}

				int floorSpriteX = (index % (textureWidth / spriteWidth));
				int floorSpriteY = 0;
				Vector2fc tc2 = new Vector2f(sprite.x() * floorSpriteX, sprite.y() * floorSpriteY);
				Vector2fc tc3 = tc2.add(sprite, new Vector2f());

				boolean north = i - size < 0 || map[i - size] != tile;
				boolean east = i + 1 >= map.length || map[i + 1] != tile;
				boolean west = i - 1 < 0 || map[i - 1] != tile;
				boolean south = i + size >= map.length || map[i + size] != tile;

				modelBuilder.addTexturedBox(wallPos, wallPos.add(1, 2, 1, new Vector3f()),
						tc0, tc1,
						tc2, tc3,
						tc2, tc3,
						tc2, tc3,
						tc2, tc3,
						tc2, tc3,
						true, false, south, north, west, east);
			}
		}

		return modelBuilder.bake(true, false);
	}

	public static MeshData toMeshData(World world, int texWidth, int texHeight)
	{
		MeshBuilder modelBuilder = new MeshBuilder();

		int[] map = world.getMap();
		int size = world.getSize();

		for(int i = 0; i < map.length; ++i)
		{
			Vector3fc wallPos = new Vector3f(i % size, 0, i / size);

			if (map[i] != 0)
			{
				int tile = map[i];

				int textureWidth = texWidth;
				int textureHeight = texHeight;

				int spriteWidth = 16;
				int spriteHeight = 16;

				float textureSpriteWidth = spriteWidth / (float) textureWidth;
				float textureSpriteHeight = spriteHeight / (float) textureHeight;
				Vector2fc sprite = new Vector2f(textureSpriteWidth, textureSpriteHeight);

				//WALL SPRITE
				int wallSpriteX = (tile % (textureWidth / spriteWidth));
				int wallSpriteY = (tile / (textureWidth / spriteWidth)) + 1;
				Vector2fc tc0 = new Vector2f(sprite.x() * wallSpriteX, sprite.y() * wallSpriteY);
				Vector2fc tc1 = tc0.add(sprite, new Vector2f());

				//FLOOR SPRITE
				int index = 0;
				if (i + 1 < map.length && map[i + 1] == tile) //EAST WALL
				{
					index += 1;
				}
				if (i - size >= 0 && map[i - size] == tile) //NORTH WALL
				{
					index += 2;
				}
				if (i - 1 >= 0 && map[i - 1] == tile) //WEST WALL
				{
					index += 4;
				}
				if (i + size < map.length && map[i + size] == tile) //SOUTH WALL
				{
					index += 8;
				}

				int floorSpriteX = (index % (textureWidth / spriteWidth));
				int floorSpriteY = 0;
				Vector2fc tc2 = new Vector2f(sprite.x() * floorSpriteX, sprite.y() * floorSpriteY);
				Vector2fc tc3 = tc2.add(sprite, new Vector2f());

				//FLOOR
				modelBuilder.addFaceHorizontal(wallPos, wallPos.add(1, 0, 1, new Vector3f()), tc2, tc3);

				//WALL
				if (i - size < 0 || map[i - size] != tile) //NORTH WALL
				{
					modelBuilder.addFaceVertical(wallPos, wallPos.add(1, 1, 0, new Vector3f()), tc0, tc1);
				}
				if (i + 1 >= map.length || map[i + 1] != tile) //EAST WALL
				{
					modelBuilder.addFaceVertical(wallPos.add(1, 0, 0, new Vector3f()), wallPos.add(1, 1, 1, new Vector3f()), tc0, tc1);
				}
				if (i + size >= map.length || map[i + size] != tile) //SOUTH WALL
				{
					modelBuilder.addFaceVertical(wallPos.add(0, 0, 1, new Vector3f()), wallPos.add(1, 1, 1, new Vector3f()), tc0, tc1);
				}
				if (i - 1 < 0 || map[i - 1] != tile) //WEST WALL
				{
					modelBuilder.addFaceVertical(wallPos, wallPos.add(0, 1, 1, new Vector3f()), tc0, tc1);
				}
			}
		}

		return modelBuilder.bake(true, false);
	}
}
