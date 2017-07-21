package net.jimboi.boron.stage_a.shroom;

import net.jimboi.apricot.stage_c.hoob.collision.CollisionManager;
import net.jimboi.boron.base.SceneBase;
import net.jimboi.boron.stage_a.shroom.component.LivingEntity;

import org.bstone.living.LivingManager;
import org.bstone.transform.Transform3c;
import org.bstone.util.direction.Direction;
import org.bstone.window.camera.CameraController;
import org.bstone.window.view.ScreenSpace;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class SceneShroomBase<R extends RenderShroomBase> extends SceneBase<R> implements LivingManager.OnLivingAddListener<LivingEntity>, LivingManager.OnLivingRemoveListener<LivingEntity>
{
	protected CameraController cameraController;

	protected LivingManager<LivingEntity> livingManager;
	protected CollisionManager collisionManager;

	protected ShroomWorld world;
	private ScreenSpace worldSpace;

	protected abstract ShroomWorld createWorld();

	protected abstract CameraController createMainCameraController();

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.cameraController = this.createMainCameraController();

		this.livingManager = new LivingManager<>();
		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);

		this.collisionManager = new CollisionManager();

		this.world = this.createWorld();
	}

	@Override
	protected void onSceneStart()
	{
		this.cameraController.start(this.getRender().getMainCamera());

		this.worldSpace = new ScreenSpace(Shroom.ENGINE.getWindow().getCurrentViewPort(), this.getRender().getMainCamera(), Direction.CENTER, true, false);
		this.world.create();
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		super.onSceneUpdate(delta);

		this.cameraController.update(delta);
		this.livingManager.update(delta);
		this.world.update(delta);
	}

	@Override
	protected void onSceneStop()
	{
		super.onSceneStop();

		this.world.destroy();

		this.cameraController.stop();
	}

	@Override
	protected void onSceneDestroy()
	{
		super.onSceneDestroy();

		this.cameraController = null;
	}

	public void spawn(LivingEntity living)
	{
		this.getLivingManager().add(living);
	}

	@Override
	public void onLivingAdd(LivingEntity living)
	{
		Entity entity = this.getEntityManager().createEntity(living);
		living.onEntityCreate(entity);
	}

	@Override
	public void onLivingRemove(LivingEntity living)
	{
		Entity entity = this.getEntityManager().getEntityByComponent(living);
		entity.setDead();
		living.onEntityDestroy(entity);
	}

	public CameraController getMainCameraController()
	{
		return this.cameraController;
	}

	public Transform3c getMainCameraTransform()
	{
		return this.getRender().getMainCamera().transform();
	}

	public LivingManager<LivingEntity> getLivingManager()
	{
		return this.livingManager;
	}

	public CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}

	public ShroomWorld getWorld()
	{
		return this.world;
	}

	public ScreenSpace getWorldSpace()
	{
		return this.worldSpace;
	}
}
