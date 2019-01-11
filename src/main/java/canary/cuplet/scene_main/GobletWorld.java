package canary.cuplet.scene_main;

import canary.base.MaterialProperty;
import canary.base.Model;
import canary.base.collisionbox.CollisionBoxManager;
import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.base.collisionbox.collider.BoxCollider;
import canary.cuplet.Cuplet;
import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.scene_main.component.SystemDamageable;
import canary.cuplet.scene_main.component.SystemMotion;
import canary.cuplet.scene_main.entity.EntityPlayer;
import canary.cuplet.scene_main.entity.EntityRat;
import canary.cuplet.scene_main.entity.EntitySkeleton;
import canary.cuplet.scene_main.tile.Tile;
import canary.cuplet.scene_main.tile.TileMap;
import canary.cuplet.scene_main.tile.TileMapModelManager;
import canary.cuplet.scene_main.tile.Tiles;

import canary.bstone.asset.AssetManager;
import canary.bstone.gameobject.GameObject;
import canary.bstone.material.Material;
import canary.bstone.mogli.Mesh;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.transform.Transform3;
import canary.bstone.transform.Transform3c;
import canary.bstone.util.ColorUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletWorld
{
	private final GobletEntityManager entityManager;
	private final Random random = new Random();

	private GobletCameraController cameraController;

	private SystemMotion motionSystem;
	private SystemDamageable damageableSystem;

	private EntityPlayer player;

	private TileMapModelManager tileMapModelManager;
	private TileMap tilemap;

	//private RoomModelManager roomModelManager;
	//private Room room;

	public GobletWorld()
	{
		this.entityManager = new GobletEntityManager();
		this.motionSystem = new SystemMotion(this.entityManager);
		this.damageableSystem = new SystemDamageable(this.entityManager);
	}

	public void start()
	{
		this.cameraController = new GobletCameraController();
		this.cameraController.start(((MainRenderer) Cuplet.getCuplet().getSceneManager().getCurrentRenderer()).getCamera());

		this.motionSystem.start();
		this.damageableSystem.start();

		Transform3 transform = new Transform3();
		this.player = new EntityPlayer(this, transform);
		this.spawnEntity(this.player);
		this.cameraController.setTarget(transform);

		/*
		this.room = new Room(0, 0, 30, 30);
		for(int i = 0; i < this.room.getWidth(); ++i)
		{
			for(int j = 0; j < this.room.getHeight(); ++j)
			{
				if (!this.room.isSolid(i, j))
				{
					if (this.random.nextInt(40) == 0)
					{
						this.spawnEntity(new EntityRat(this, new Transform3().setPosition(i + 0.5F, j + 0.5F, 0)));
					}
					else if (this.random.nextInt(50) == 0)
					{
						this.spawnEntity(new EntitySkeleton(this, new Transform3().setPosition(i + 0.5F, j + 0.5F, 0)));
					}
				}
			}
		}
		this.getBoundingManager().addCollider(this.room);

		this.roomModelManager = new RoomModelManager(Asset.wrap(Goblet.getGoblet().getRender().texFont), Asset.wrap(Goblet.getGoblet().getRender().atsFont));
		Model model = this.roomModelManager.createStaticRoom(this.room);
		Goblet.getGoblet().getRender().renderables.add(new RenderableBase(model, new Matrix4f()));
		*/

		this.tilemap = new TileMap(0, 0, 30, 30);
		for(int i = 0; i < this.tilemap.getWidth(); ++i)
		{
			for(int j = 0; j < this.tilemap.getHeight(); ++j)
			{
				Tile tile = Math.random() < 0.8F ? Tiles.dirt : Tiles.stone;
				this.tilemap.setTileByMap(i, j, tile);

				if (!tile.isSolid())
				{
					if (this.random.nextInt(40) == 0)
					{
						this.spawnEntity(new EntityRat(this, new Transform3().setPosition(i + 0.5F, j + 0.5F, 0)));
					}
					else if (this.random.nextInt(50) == 0)
					{
						this.spawnEntity(new EntitySkeleton(this, new Transform3().setPosition(i + 0.5F, j + 0.5F, 0)));
					}
				}
			}
		}
		this.getColliders().add(this.tilemap);

		final AssetManager assets = Cuplet.getCuplet().getAssetManager();

		this.tileMapModelManager = new TileMapModelManager(
				assets.getAsset("texture", "font"),
				assets.getAsset("texture_atlas", "font"));
		Model model = this.tileMapModelManager.createStaticModel(this.tilemap);
		((MainRenderer) Cuplet.getCuplet().getSceneManager().getCurrentRenderer()).renderables.add(new RenderableBase(model, new Matrix4f()));
	}

	public void stop()
	{
		//this.roomModelManager.destroy();
		this.tileMapModelManager.destroy();

		this.damageableSystem.stop();
		this.motionSystem.stop();

		this.cameraController.stop();

		this.entityManager.clear();
	}

	public void update()
	{
		this.cameraController.update(1);
		this.entityManager.update();
		this.damageableSystem.update();
		this.motionSystem.update();
	}

	public void spawnEntity(GobletEntity entity)
	{
		this.entityManager.addGameObject(entity);
	}

	public Transform3 createTransform(Transform3c transform)
	{
		Transform3 result = new Transform3();
		result.position.set(transform.position3());
		result.rotation.set(transform.quaternion());
		result.scale.set(transform.scale3());
		return result;
	}

	public AxisAlignedBoundingBox createBoundingBox(Transform3c transform, float size)
	{
		final Vector3fc pos = transform.position3();
		return new AxisAlignedBoundingBox(pos.x(), pos.y(), size / 2F, size / 2F);
	}

	public ComponentRenderable createRenderable2D(Transform3 transform, char c, int color)
	{
		return new ComponentRenderable(transform, this.createModel2D(c, color));
	}

	public Model createModel2D(char c, int color)
	{
		return new Model(Cuplet.getCuplet().getAssetManager().<Mesh>getAsset("mesh", "quad"), this.createMaterial2D(c, color));
	}

	public Material createMaterial2D(char c, int color)
	{
		Material material = new Material();
		material.setProperty(MaterialProperty.COLOR, new Vector4f(
				ColorUtil.getRed(color) / 255F,
				ColorUtil.getGreen(color) / 255F,
				ColorUtil.getBlue(color) / 255F,
				1
		));
		SubTexture subtexture = ((MainRenderer) Cuplet.getCuplet().getSceneManager().getCurrentRenderer()).fontSheet.get(c);
		material.setProperty(MaterialProperty.TEXTURE, subtexture.getTextureAtlas().getTexture());
		material.setProperty(MaterialProperty.SPRITE_OFFSET, new Vector2f(subtexture.getU(), subtexture.getV()));
		material.setProperty(MaterialProperty.SPRITE_SCALE, new Vector2f(subtexture.getWidth(), subtexture.getHeight()));
		return material;
	}

	@SuppressWarnings("unchecked")
	public <T extends GobletEntity> T getNearestEntity(float x, float y, Class<? extends T> entity)
	{
		GobletEntity nearest = null;
		float distance = -1F;
		Iterable<GameObject> objects = this.entityManager.getGameObjects();
		for(GameObject obj : objects)
		{
			if (entity.isAssignableFrom(obj.getClass()))
			{
				GobletEntity gobletEntity = (GobletEntity) obj;
				float f = gobletEntity.getTransform().position3().distanceSquared(x, y, 0);
				if (nearest == null || f < distance)
				{
					nearest = gobletEntity;
					distance = f;
				}
			}
		}
		return (T) nearest;
	}

	public TileMap getRoom(float x, float y)
	{
		return this.tilemap;
	}

	/*
	public Room getRoom(float x, float y)
	{
		return this.room;
	}
	*/

	public Random getRandom()
	{
		return this.random;
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	public GobletEntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public CollisionBoxManager getBoundingManager()
	{
		return this.entityManager.getBoundingManager();
	}

	public Set<BoxCollider> getColliders()
	{
		return this.entityManager.getColliders();
	}
}
