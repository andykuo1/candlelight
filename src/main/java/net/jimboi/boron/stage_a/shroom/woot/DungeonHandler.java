package net.jimboi.boron.stage_a.shroom.woot;

import net.jimboi.boron.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.shroom.Shroom;
import net.jimboi.boron.stage_a.shroom.component.OldLivingEntity;
import net.jimboi.boron.stage_a.shroom.woot.collision.CollisionManager;
import net.jimboi.boron.stage_a.shroom.woot.collision.Shape;
import net.jimboi.boron.stage_a.shroom.woot.collision.StaticCollider;

import org.bstone.material.MaterialManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.resource.MeshLoader;
import org.qsilver.resource.TextureAtlasLoader;
import org.qsilver.util.map2d.IntMap;
import org.zilar.dungeon.DungeonBuilder;
import org.zilar.dungeon.DungeonData;
import org.zilar.dungeon.maze.MazeDungeonBuilder;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyTexture;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;
import org.zilar.sprite.TextureAtlasData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/19/17.
 */
public class DungeonHandler
{
	private final SceneWoot scene;

	private OldLivingEntity dungeon;
	private DungeonData data;

	private boolean[] solids;
	private List<StaticCollider> boundings;
	private Asset<Mesh> mesh;
	private Asset<TextureAtlas> textureAtlas;
	private Model model;

	public DungeonHandler(SceneWoot scene)
	{
		this.scene = scene;
	}

	public OldLivingEntity generate(long seed, int width, int height)
	{
		DungeonBuilder db = new MazeDungeonBuilder(seed, width, height);
		this.data = db.bake();

		if (this.boundings != null)
		{
			for(StaticCollider bounding : this.boundings)
			{
				this.scene.getWorld().getCollisionManager().destroyStatic(bounding);
			}

			this.boundings = null;
		}

		this.boundings = this.generateBoundings(this.data, this.scene.getWorld().getCollisionManager());
		this.model = this.generateModel(this.data, Shroom.ENGINE.getAssetManager(), this.scene.getMaterialManager());

		this.solids = new boolean[this.data.width * this.data.height];
		for(int i = 0; i < this.data.getTiles().size(); ++i)
		{
			this.solids[i] = this.data.getTiles().get(i) == 0;
		}

		//CREATE LIVING

		Transform3 transform = new Transform3();
		return this.dungeon = this.scene.getLivingManager().add(new OldLivingEntity(
				this.scene.getWorld(),
				transform,
				null,
				new EntityComponentRenderable(transform, this.model)
		));
	}

	protected List<StaticCollider> generateBoundings(DungeonData data, CollisionManager collisionManager)
	{
		List<StaticCollider> list = new ArrayList<>();

		int tiles = 0;
		for (int y = 0; y < data.height; ++y)
		{
			for (int x = 0; x < data.width; ++x)
			{
				boolean flag = false;
				int tile = data.getTiles().get(x, y);
				if (tile == 0)
				{
					tiles++;
					if (x == data.width - 1)
					{
						x++;
						flag = true;
					}
				}
				else
				{
					flag = true;
				}

				if (flag)
				{
					if (tiles > 0)
					{
						StaticCollider collider = collisionManager.createStatic(new Shape.AABB(x - tiles / 2F, y + 0.5F, tiles, 1));
						tiles = 0;
						list.add(collider);
					}
				}
			}
		}

		return list;
	}

	protected Model generateModel(DungeonData data, AssetManager assetManager, MaterialManager materialManager)
	{
		final Asset<Texture> font = assetManager.getAsset(Texture.class, "font");

		if (this.textureAtlas == null)
		{
			TextureAtlasBuilder tab = new TextureAtlasBuilder();
			tab.addTileSheet(font, 0, 0, 16, 16, 0, 0, 16, 16);
			TextureAtlasData atlas = tab.bake();
			tab.clear();
			this.textureAtlas = assetManager.registerAsset(TextureAtlas.class, "font", new TextureAtlasLoader.TextureAtlasParameter(atlas));
		}

		MeshData meshData = createMeshFromMap(data.getTiles(), this.textureAtlas);

		if (this.mesh == null)
		{
			this.mesh = assetManager.registerAsset(Mesh.class, "dungeon", new MeshLoader.MeshParameter(meshData));
		}
		else
		{
			ModelUtil.updateMesh(this.mesh.getSource(), meshData);
		}

		return new Model(this.mesh,
				materialManager.createMaterial(
						new PropertyTexture(font).setTransparent(false)
				),
				"simple"
		);
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
				return new Vector3f(0, 0, 0);
			}
		}
		while(this.solids[index] != solid);
		return new Vector3f(index % this.data.width, 0, index / this.data.width);
	}

	private static MeshData createMeshFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite = textureAtlas.getSource().getSprite(0);
		Vector2f texSideTopLeft = new Vector2f(sprite.getU(), sprite.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(sprite.getWidth(), sprite.getHeight());

		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f();
		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
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
		if (x < 0 || x >= map.width || y < 0 || y >= map.height) return true;
		return map.get(x, y) == 0;
	}
}
