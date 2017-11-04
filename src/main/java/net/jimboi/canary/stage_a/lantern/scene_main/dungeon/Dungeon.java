package net.jimboi.canary.stage_a.lantern.scene_main.dungeon;

import net.jimboi.canary.stage_a.base.collisionbox.box.GridBasedBoundingBox;

import org.bstone.asset.Asset;
import org.bstone.sprite.textureatlas.SubTexture;
import org.bstone.sprite.textureatlas.TextureAtlas;
import org.bstone.util.gridmap.IntMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;

/**
 * Created by Andy on 11/3/17.
 */
public class Dungeon
{
	public static GridBasedBoundingBox createBoundingBoxFromMap(IntMap map)
	{
		GridBasedBoundingBox gbbb = new GridBasedBoundingBox(0, 0, map.getWidth(), map.getHeight());

		for (int y = 0; y < map.getHeight(); ++y)
		{
			for (int x = 0; x < map.getWidth(); ++x)
			{
				if (isSolid(map, x, y))
				{
					gbbb.setSolid(x + y * gbbb.getWidth(), true);
				}
			}
		}

		return gbbb;
	}

	public static MeshData createMeshFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		SubTexture subTexture = textureAtlas.get().getSubTexture(0);
		Vector2f texSideTopLeft = new Vector2f(subTexture.getU(), subTexture.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(subTexture.getWidth(), subTexture.getHeight());

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
					if (!isSolid(map, x + 1, y)) tile += 1;
					if (!isSolid(map, x, y - 1)) tile += 2;
					if (!isSolid(map, x - 1, y)) tile += 4;
					if (!isSolid(map, x, y + 1)) tile += 8;
					subTexture = textureAtlas.get().getSubTexture(tile);
					texTopLeft.set(subTexture.getU(), subTexture.getV());
					texBotRight.set(texTopLeft).add(subTexture.getWidth(), subTexture.getHeight());

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
					if (isSolid(map, x + 1, y)) tile += 1;
					if (isSolid(map, x, y - 1)) tile += 2;
					if (isSolid(map, x - 1, y)) tile += 4;
					if (isSolid(map, x, y + 1)) tile += 8;
					subTexture = textureAtlas.get().getSubTexture(tile + 9 * 16);
					texTopLeft.set(subTexture.getU(), subTexture.getV());
					texBotRight.set(texTopLeft).add(subTexture.getWidth(), subTexture.getHeight());

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
