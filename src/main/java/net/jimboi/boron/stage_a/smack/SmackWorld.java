package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.smack.chunk.Chunk;
import net.jimboi.boron.stage_a.smack.chunk.ChunkManager;
import net.jimboi.boron.stage_a.smack.entity.EntityPlayer;
import net.jimboi.boron.stage_a.smack.tile.DungeonHandler;
import net.jimboi.boron.stage_a.smack.tile.DungeonModelManager;
import net.jimboi.boron.stage_a.smack.tile.LevelGenerator;

import org.bstone.livingentity.LivingEntity;
import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.render.material.PropertyTexture;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.qsilver.asset.Asset;

import java.util.Random;

/**
 * Created by Andy on 8/5/17.
 */
public class SmackWorld
{
	private final SmackEntityManager smackManager = new SmackEntityManager();
	private FollowCameraController cameraController;
	private EntityPlayer player;
	private Random rand;

	private DungeonModelManager dungeonModelManager;
	private DungeonHandler dungeonHandler;

	private ChunkManager chunkManager;

	public SmackWorld()
	{
		this.rand = new Random();
	}

	public void onCreate()
	{
		this.dungeonModelManager = new DungeonModelManager(Asset.wrap(Smack.getSmack().texFont), Asset.wrap(Smack.getSmack().atsFont));
		this.dungeonHandler = new DungeonHandler(this, 0);

		this.chunkManager = new ChunkManager();

		Transform3 transform = new Transform3();
		Vector2f pos = LevelGenerator.getRandomSpawn(this.rand, this.dungeonHandler.getCurrentLevel().getTileMap(), new Vector2f());
		transform.setPosition(pos.x(), pos.y(), 0);
		this.player = new EntityPlayer(this, transform);
		this.cameraController = new FollowCameraController();
		this.cameraController.start(Smack.getSmack().camera);
		this.cameraController.setTarget(transform);
		this.smackManager.addLivingEntity(this.player);

		//this.smackManager.spawn(new EntityMonster(this, new Transform3().setPosition(1, 1, 1)));
		/*
		Model model = this.dungeonModelManager.createStaticDungeon(this.dungeonHandler.getCurrentLevel().getTileMap());
		//TODO: I believe this is the only place where it has a z offset!
		Smack.getSmack().renderables.add(new RenderableBase(model, new Matrix4f().translation(0, 0, -0.1F)));
		this.smackManager.getBoundings().add(this.dungeonHandler);
		 */
	}

	public void onDestroy()
	{
		this.cameraController.stop();
		this.smackManager.clear();
	}

	public void onUpdate()
	{
		this.smackManager.update();
		this.chunkManager.update(this);

		this.cameraController.update(1);

		Chunk chunk = this.chunkManager.loadChunk(this, this.player.getTransform().position3().x(), this.player.getTransform().position3().y());

		/*
		if (this.rand.nextInt(100) == 0)
		{
			float x = this.rand.nextFloat() * 10;
			float y = this.rand.nextFloat() * 10;
			this.smackManager.spawn(new EntityMonster(this, new Transform3().setPosition(x, y, 0)));
		}

		if (this.rand.nextInt(100) == 0)
		{
			float x = this.rand.nextFloat() * 10;
			float y = this.rand.nextFloat() * 10;
			this.smackManager.spawn(new EntityBoulder(this, new Transform3().setPosition(x, y, 0), this.rand.nextFloat() + 0.5F));
		}
		*/
	}

	public void spawn(SmackEntity entity)
	{
		this.smackManager.addLivingEntity(entity);
	}

	public AxisAlignedBoundingBox createBoundingBox(float x, float y, float width, float height)
	{
		return new AxisAlignedBoundingBox(x, y, width / 2F, height / 2F);
	}

	public ComponentRenderable createRenderable2D(Transform3 transform, char c, int color)
	{
		Material material = new Material();
		material.addProperty(PropertyTexture.PROPERTY);
		material.addProperty(PropertyColor.PROPERTY);

		PropertyTexture.PROPERTY.bind(material)
				.setSprite(Smack.getSmack().fontSheet.get(c))
				.unbind();
		PropertyColor.PROPERTY.bind(material)
				.setColor(color)
				.unbind();

		return new ComponentRenderable(transform,
				new Model(Asset.wrap(Smack.getSmack().quad), material)
		);
	}

	@SuppressWarnings("unchecked")
	public <T extends SmackEntity> T getNearestEntity(float x, float y, Class<? extends T> entity)
	{
		SmackEntity nearest = null;
		float distance = -1F;
		Iterable<LivingEntity> livings = this.smackManager.getLivingEntities();
		for(LivingEntity living : livings)
		{
			if (entity.isAssignableFrom(living.getClass()))
			{
				SmackEntity smackEntity = (SmackEntity) living;
				float f = smackEntity.getTransform().position3().distanceSquared(x, y, 0);
				if (nearest == null || f < distance)
				{
					nearest = smackEntity;
					distance = f;
				}
			}
		}
		return (T) nearest;
	}

	public Random getRandom()
	{
		return this.rand;
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	public SmackEntityManager getSmacks()
	{
		return this.smackManager;
	}

	public DungeonHandler getDungeonHandler()
	{
		return this.dungeonHandler;
	}
}
