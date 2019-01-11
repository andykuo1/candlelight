package apricot.stage_b.glim;

import apricot.base.animation.AnimationManager;
import apricot.base.astar.AstarNavigator;
import apricot.base.astar.NavigatorMap;
import apricot.base.astar.map.NavigatorCardinalMap;
import apricot.stage_b.glim.bounding.BoundingManager;
import apricot.stage_b.glim.bounding.square.AABB;
import apricot.base.asset.Asset;
import apricot.base.gridmap.IntMap;
import apricot.base.sprite.Sprite;
import apricot.base.sprite.TextureAtlas;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import apricot.zilar.dungeon.DungeonBuilder;
import apricot.zilar.dungeon.DungeonData;
import apricot.zilar.dungeon.maze.MazeDungeonBuilder;
import apricot.zilar.meshbuilder.MeshBuilder;
import apricot.zilar.meshbuilder.MeshData;

/**
 * Created by Andy on 6/1/17.
 */
public class WorldGlim
{
	private final BoundingManager boundingManager;
	private final DungeonData data;
	private final NavigatorMap<NavigatorCardinalMap.Cell> navigatorMap;
	private boolean[] solids;

	public WorldGlim(BoundingManager boundingManager)
	{
		this.boundingManager = boundingManager;
		DungeonBuilder db = new MazeDungeonBuilder(0, 45, 45);
		this.data = db.bake();

		int tiles = 0;
		for (int y = 0; y < this.data.height; ++y)
		{
			for (int x = 0; x < this.data.width; ++x)
			{
				int tile = this.data.getTiles().get(x, y);
				if (tile == 0 && x < this.data.width - 1)
				{
					tiles++;
				}
				else if (tiles > 0)
				{
					this.boundingManager.create(new AABB(x - tiles / 2F, y + 0.5F, tiles / 2F, 0.5F));
					tiles = 0;
				}
			}
		}

		this.solids = new boolean[this.data.width * this.data.height];
		for(int i = 0; i < this.data.getTiles().length(); ++i)
		{
			this.solids[i] = this.data.getTiles().get(i) == 0;
		}
		this.navigatorMap = new NavigatorCardinalMap(this.solids, this.data.width, this.data.height);
	}

	public AstarNavigator<NavigatorCardinalMap.Cell> createNavigator()
	{
		return new AstarNavigator<>(this.navigatorMap);
	}

	public Vector3f getRandomTilePos(boolean solid)
	{
		int attempts = 0;
		int index;
		do
		{
			index = (int)(Math.random() * this.solids.length);
			attempts++;
			if (attempts > 100)
			{
				return null;
			}
		}
		while(this.solids[index] != solid);
		return new Vector3f(index % this.data.width, 0, index / this.data.width);
	}

	public boolean[] getSolids()
	{
		return this.solids;
	}

	public IntMap getMap()
	{
		return this.data.getTiles();
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}

	private static AnimationManager.Animator<Integer> animator;
	public static MeshData createMeshFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite = textureAtlas.getSource().getSprite(0);
		Vector2f texSideTopLeft = new Vector2f(sprite.getU(), sprite.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(sprite.getWidth(), sprite.getHeight());

		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f();
		for (int x = 0; x < map.getWidth(); ++x)
		{
			for (int y = 0; y < map.getHeight(); ++y)
			{
				Vector3fc wallPos = new Vector3f(x, 0, y);
				u.set(0);
				v.set(0);

				if (!isSolid(map, x, y))
				{
					int tile = 0;
					if (!isSolid(map, x - 1, y)) tile += 1;
					if (!isSolid(map, x, y + 1)) tile += 2;
					if (!isSolid(map, x + 1, y)) tile += 4;
					if (!isSolid(map, x, y - 1)) tile += 8;
					sprite = textureAtlas.getSource().getSprite(tile);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					mb.addBox(wallPos.add(0, -1, 0, u),
							wallPos.add(1, 0, 1, v),
							texTopLeft, texBotRight,
							false,
							true,
							false,
							false,
							false,
							false);
				}
				else
				{
					int tile = 0;
					if (isSolid(map, x - 1, y)) tile += 1;
					if (isSolid(map, x, y + 1)) tile += 2;
					if (isSolid(map, x + 1, y)) tile += 4;
					if (isSolid(map, x, y - 1)) tile += 8;
					sprite = textureAtlas.getSource().getSprite(tile + 9 * 16);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					boolean front = !isSolid(map, x, y + 1);
					boolean back = !isSolid(map, x, y - 1);
					boolean left = !isSolid(map, x - 1, y);
					boolean right = !isSolid(map, x + 1, y);

					mb.addTexturedBox(wallPos,
							wallPos.add(1, 2, 1, v),
							texSideTopLeft, texSideBotRight,//Front
							texSideTopLeft, texSideBotRight,//Back
							texTopLeft, texBotRight,//Top
							texTopLeft, texBotRight,//Bot
							texSideTopLeft, texSideBotRight,//Left
							texSideTopLeft, texSideBotRight,//Right
							false,
							true,
							front,
							back,
							left,
							right,
							true);
				}
			}
		}

		return mb.bake(false, false);
	}

	private static boolean isSolid(IntMap map, int x, int y)
	{
		if (!map.containsKey(x, y)) return true;
		return map.get(x, y) == 0;
	}
}
