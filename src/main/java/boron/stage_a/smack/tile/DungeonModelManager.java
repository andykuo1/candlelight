package boron.stage_a.smack.tile;

import boron.base.asset.Asset;
import boron.base.gridmap.IntMap;
import boron.base.render.material.Material;
import boron.base.render.material.PropertyTexture;
import boron.base.render.model.Model;
import boron.base.sprite.Sprite;
import boron.base.sprite.TextureAtlas;

import boron.bstone.mogli.Mesh;
import boron.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import boron.zilar.meshbuilder.MeshBuilder;
import boron.zilar.meshbuilder.MeshData;
import boron.zilar.meshbuilder.ModelUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 8/7/17.
 */
public class DungeonModelManager
{
	private Asset<Texture> texture;
	private Asset<TextureAtlas> textureAtlas;
	private Set<Mesh> meshes = new HashSet<>();
	private Material material;

	public DungeonModelManager(Asset<Texture> texture, Asset<TextureAtlas> textureAtlas)
	{
		this.texture = texture;
		this.textureAtlas = textureAtlas;

		this.material = new Material();
		this.material.addProperty(PropertyTexture.PROPERTY);
		PropertyTexture.PROPERTY.bind(this.material)
				.setTexture(this.texture)
				.unbind();
	}

	public void destroy()
	{
		for(Mesh mesh : this.meshes)
		{
			try
			{
				mesh.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.meshes.clear();
	}

	public Model createStaticDungeon(IntMap tiles)
	{
		Mesh mesh = ModelUtil.createStaticMesh(createMesh3DFromMap(tiles, this.textureAtlas));
		this.meshes.add(mesh);
		return new Model(Asset.wrap(mesh), this.material);
	}

	private static MeshData createMesh2DFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite;
		Vector2f vec = new Vector2f();

		Random rand = new Random();
		for (int x = 0; x < map.getWidth(); ++x)
		{
			for (int y = 0; y < map.getHeight(); ++y)
			{
				Vector2fc wallPos = new Vector2f(x, y);

				int tile = map.get(x, y);
				if (tile != 0)
				{
					tile = 9 * 16;
					if (!isSolid(map, x + 1, y)) tile += 1;
					if (!isSolid(map, x, y + 1)) tile += 2;
					if (!isSolid(map, x - 1, y)) tile += 4;
					if (!isSolid(map, x, y - 1)) tile += 8;
				}
				else
				{
					tile = 0;
					/*
					if (isSolid(map, x + 1, y)) tile += 1;
					if (isSolid(map, x, y + 1)) tile += 2;
					if (isSolid(map, x - 1, y)) tile += 4;
					if (isSolid(map, x, y - 1)) tile += 8;
					*/
				}
				sprite = textureAtlas.getSource().getSprite(tile);
				texTopLeft.set(sprite.getU(), sprite.getV());
				texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

				mb.addPlane(wallPos, wallPos.add(1, 1, vec), 0, texTopLeft, texBotRight);
			}
		}

		return mb.bake(false, false);
	}

	public static MeshData createMesh3DFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite = textureAtlas.getSource().getSprite(0);
		Vector2f texSideTopLeft = new Vector2f(sprite.getU(), sprite.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(sprite.getWidth(), sprite.getHeight());

		Vector3f pos = new Vector3f();
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f();
		for (int x = 0; x < map.getWidth(); ++x)
		{
			for (int y = 0; y < map.getHeight(); ++y)
			{
				pos.set(x, y, -1);
				u.set(0);
				v.set(0);

				boolean center = isSolid(map, x, y);
				boolean up = isSolid(map, x, y + 1);
				boolean down = isSolid(map, x, y - 1);
				boolean left = isSolid(map, x + 1, y);
				boolean right = isSolid(map, x - 1, y);

				if (!center)
				{
					int tile = 0;
					if (!left) tile += 1;
					if (!up) tile += 2;
					if (!right) tile += 4;
					if (!down) tile += 8;

					sprite = textureAtlas.getSource().getSprite(tile);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					mb.addBox(pos,
							pos.add(1, 1, 0, v),
							texTopLeft, texBotRight,
							false,
							false,
							true,
							false,
							false,
							false);
				}
				else
				{
					int tile = 0;
					if (left) tile += 1;
					if (up) tile += 2;
					if (right) tile += 4;
					if (down) tile += 8;
					sprite = textureAtlas.getSource().getSprite(tile + 9 * 16);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					mb.addTexturedBox(pos, pos.add(1, 1, 2, v),
							texTopLeft, texBotRight,
							texTopLeft, texBotRight,
							texSideTopLeft, texSideBotRight,
							texSideTopLeft, texSideBotRight,
							texSideTopLeft, texSideBotRight,
							texSideTopLeft, texSideBotRight,
							!down,
							!up,
							true,
							false,
							!right,
							!left,
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
