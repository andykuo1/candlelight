package net.jimboi.boron.stage_a.goblet;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.goblet.component.SystemDamageable;
import net.jimboi.boron.stage_a.goblet.component.SystemMotion;
import net.jimboi.boron.stage_a.goblet.entity.EntityPlayer;
import net.jimboi.boron.stage_a.goblet.entity.EntityRat;
import net.jimboi.boron.stage_a.goblet.entity.EntitySkeleton;
import net.jimboi.boron.stage_a.goblet.entity.EntityVillager;
import net.jimboi.boron.stage_a.goblet.tile.Tile;
import net.jimboi.boron.stage_a.goblet.tile.TileMap;
import net.jimboi.boron.stage_a.goblet.tile.TileMapModelManager;
import net.jimboi.boron.stage_a.goblet.tile.Tiles;

import org.bstone.livingentity.LivingEntity;
import org.bstone.render.RenderableBase;
import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.render.material.PropertyTexture;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.joml.Matrix4f;
import org.joml.Vector3fc;
import org.qsilver.asset.Asset;

import java.util.Random;

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
		this.cameraController.start(Goblet.getGoblet().getRender().getCamera());

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
					else if (this.random.nextInt(60) == 0)
					{
						this.spawnEntity(new EntityVillager(this, new Transform3().setPosition(i + 0.5F, j + 0.5F, 0)));
					}
				}
			}
		}
		this.getBoundingManager().addCollider(this.tilemap);

		this.tileMapModelManager = new TileMapModelManager(Asset.wrap(Goblet.getGoblet().getRender().texFont), Asset.wrap(Goblet.getGoblet().getRender().atsFont));
		Model model = this.tileMapModelManager.createStaticModel(this.tilemap);
		Goblet.getGoblet().getRender().renderables.add(new RenderableBase(model, new Matrix4f()));
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
	}

	public void spawnEntity(GobletEntity entity)
	{
		this.entityManager.addLivingEntity(entity);
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
		return new Model(Asset.wrap(Goblet.getGoblet().getRender().mshQuad), this.createMaterial2D(c, color));
	}

	public Material createMaterial2D(char c, int color)
	{
		Material material = new Material();
		material.addProperty(PropertyTexture.PROPERTY);
		material.addProperty(PropertyColor.PROPERTY);

		PropertyTexture.PROPERTY.bind(material)
				.setSprite(Goblet.getGoblet().getRender().fontSheet.get(c))
				.unbind();
		PropertyColor.PROPERTY.bind(material)
				.setColor(color)
				.unbind();

		return material;
	}

	@SuppressWarnings("unchecked")
	public <T extends GobletEntity> T getNearestEntity(float x, float y, Class<? extends T> entity)
	{
		GobletEntity nearest = null;
		float distance = -1F;
		Iterable<LivingEntity> livings = this.entityManager.getLivingEntities();
		for(LivingEntity living : livings)
		{
			if (entity.isAssignableFrom(living.getClass()))
			{
				GobletEntity gobletEntity = (GobletEntity) living;
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
}
