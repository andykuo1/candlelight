package net.jimboi.boron.stage_a.smack;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.smack.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.smack.entity.EntityPlayer;
import net.jimboi.boron.stage_a.smack.tile.DungeonHandler;
import net.jimboi.boron.stage_a.smack.tile.DungeonModelManager;

import org.bstone.render.RenderableBase;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform3;
import org.joml.Matrix4f;
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
		this.dungeonModelManager = new DungeonModelManager(Smack.getSmack().materialManager, Asset.wrap(Smack.getSmack().texFont), Asset.wrap(Smack.getSmack().atsFont), "simple");
		this.dungeonHandler = new DungeonHandler();

		this.chunkManager = new ChunkManager();

		Transform3 transform = new Transform3();
		this.player = new EntityPlayer(this, transform);
		this.cameraController = new FollowCameraController();
		this.cameraController.start(Smack.getSmack().camera);
		this.cameraController.setTarget(transform);
		this.smackManager.spawn(this.player);

		//this.smackManager.spawn(new EntityMonster(this, new Transform3().setPosition(1, 1, 1)));
		Model model = this.dungeonModelManager.createStaticDungeon(this.dungeonHandler.getTiles());
		Smack.getSmack().renderables.add(new RenderableBase(model, new Matrix4f().translation(0, 0, -1)));
		this.smackManager.getBoundingManager().add(this.dungeonHandler);
	}

	public void onDestroy()
	{
		this.cameraController.stop();
		this.smackManager.destroy();
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
		this.smackManager.spawn(entity);
	}

	public AxisAlignedBoundingBox createBoundingBox(float x, float y, float width, float height)
	{
		return new AxisAlignedBoundingBox(x, y, width / 2F, height / 2F);
	}

	public EntityComponentRenderable createRenderable2D(Transform3 transform, char c, int color)
	{
		return new EntityComponentRenderable(transform,
				new Model(Asset.wrap(Smack.getSmack().quad),
				Smack.getSmack().materialManager.createMaterial(
						new PropertyTexture(Smack.getSmack().fontSheet.get(c)),
						new PropertyColor(color)
				), "simple"));
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
