package canary.cuplet.scene_main.tile;

import canary.base.MaterialProperty;
import canary.base.Model;
import canary.cuplet.Cuplet;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.material.Material;
import canary.bstone.mogli.Mesh;
import canary.bstone.mogli.Texture;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import canary.zilar.meshbuilder.MeshBuilder;
import canary.zilar.meshbuilder.MeshData;
import canary.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 9/1/17.
 */
public class TileMapModelManager
{
	private Asset<Texture> texture;
	private Asset<TextureAtlas> textureAtlas;
	private Material material;

	public TileMapModelManager(Asset<Texture> texture, Asset<TextureAtlas> textureAtlas)
	{
		this.texture = texture;
		this.textureAtlas = textureAtlas;

		this.material = new Material();
		this.material.setProperty(MaterialProperty.TEXTURE, this.texture);
	}

	public void destroy()
	{
	}

	public Model createStaticModel(TileMap tilemap)
	{
		AssetManager assets = Cuplet.getCuplet().getAssetManager();
		Mesh mesh = ModelUtil.createStaticMesh(createMesh3DFromMap(tilemap, this.textureAtlas));
		String name = mesh.toString();
		assets.cacheResource("mesh", name, mesh);

		Model model = new Model(assets.getAsset("mesh", name), this.material);
		model.transformation().translation(tilemap.getOffsetX(), tilemap.getOffsetY(), 0);
		return model;
	}

	private static MeshData createMesh2DFromMap(TileMap tilemap, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		SubTexture subtexture;
		Vector2f vec = new Vector2f();

		for (int x = 0; x < tilemap.getWidth(); ++x)
		{
			for (int y = 0; y < tilemap.getHeight(); ++y)
			{
				Vector2fc wallPos = new Vector2f(x, y);

				int tile = tilemap.getTileByMap(x, y).getID();
				if (tile == 0)
				{
					tile = 9 * 16;
					if (!isSolid(tilemap, x + 1, y)) tile += 1;
					if (!isSolid(tilemap, x, y + 1)) tile += 2;
					if (!isSolid(tilemap, x - 1, y)) tile += 4;
					if (!isSolid(tilemap, x, y - 1)) tile += 8;
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
				subtexture = textureAtlas.get().getSubTexture(tile);
				texTopLeft.set(subtexture.getU(), subtexture.getV());
				texBotRight.set(texTopLeft).add(subtexture.getWidth(), subtexture.getHeight());

				mb.addPlane(wallPos, wallPos.add(1, 1, vec), 0, texTopLeft, texBotRight);
			}
		}

		return mb.bake(false, false);
	}

	public static MeshData createMesh3DFromMap(TileMap tilemap, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		SubTexture subtexture = textureAtlas.get().getSubTexture(0);
		Vector2f texSideTopLeft = new Vector2f(subtexture.getU(), subtexture.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(subtexture.getWidth(), subtexture.getHeight());

		Vector3f pos = new Vector3f();
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f();
		for (int x = 0; x < tilemap.getWidth(); ++x)
		{
			for (int y = 0; y < tilemap.getHeight(); ++y)
			{
				pos.set(x, y, -1);
				u.set(0);
				v.set(0);

				boolean center = isSolid(tilemap, x, y);
				boolean up = isSolid(tilemap, x, y - 1);
				boolean down = isSolid(tilemap, x, y + 1);
				boolean left = isSolid(tilemap, x - 1, y);
				boolean right = isSolid(tilemap, x + 1, y);

				if (!center)
				{
					int tile = 0;
					if (!left) tile += 1;
					if (!up) tile += 2;
					if (!right) tile += 4;
					if (!down) tile += 8;

					subtexture = textureAtlas.get().getSubTexture(tile);
					texTopLeft.set(subtexture.getU(), subtexture.getV());
					texBotRight.set(texTopLeft).add(subtexture.getWidth(), subtexture.getHeight());

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
					subtexture = textureAtlas.get().getSubTexture(tile + 9 * 16);
					texTopLeft.set(subtexture.getU(), subtexture.getV());
					texBotRight.set(texTopLeft).add(subtexture.getWidth(), subtexture.getHeight());

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

	private static boolean isSolid(TileMap tilemap, int mapX, int mapY)
	{
		return tilemap.getTileByMap(mapX, mapY).isSolid();
	}
}
