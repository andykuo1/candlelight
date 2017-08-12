package net.jimboi.boron.stage_a.goblet;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.render.material.PropertyTexture;
import org.bstone.render.model.Model;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.qsilver.asset.Asset;
import org.qsilver.util.map2d.IntMap;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 8/10/17.
 */
public class RoomModelManager
{
	private Asset<Texture> texture;
	private Asset<TextureAtlas> textureAtlas;
	private Set<Mesh> meshes = new HashSet<>();
	private Material material;

	public RoomModelManager(Asset<Texture> texture, Asset<TextureAtlas> textureAtlas)
	{
		this.texture = texture;
		this.textureAtlas = textureAtlas;

		this.material = new Material();
		this.material.addProperty(PropertyTexture.PROPERTY);
		this.material.addProperty(PropertyColor.PROPERTY);
		PropertyTexture.PROPERTY.bind(this.material)
				.setTexture(this.texture)
				.unbind();
	}

	public void destroy()
	{
		for (Mesh mesh : this.meshes)
		{
			mesh.close();
		}

		this.meshes.clear();
	}

	public Model createStaticRoom(Room room)
	{
		Mesh mesh = ModelUtil.createStaticMesh(createMesh3DFromMap(room.tilemap, this.textureAtlas));
		this.meshes.add(mesh);
		Model model = new Model(Asset.wrap(mesh), this.material);
		model.transformation().translation(room.offsetX, room.offsetY, 0);
		return model;
	}

	private static MeshData createMesh2DFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite;
		Vector2f vec = new Vector2f();

		Random rand = new Random();
		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
			{
				Vector2fc wallPos = new Vector2f(x, y);

				int tile = map.get(x, y);
				if (tile == 0)
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
		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
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
		if (x < 0 || x >= map.width || y < 0 || y >= map.height) return false;
		return map.get(x, y) != 0;
	}
}
