package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.SceneBase;
import net.jimboi.boron.stage_a.shroom.component.OldLivingEntity;

import org.bstone.living.LivingManager;
import org.bstone.transform.Transform3c;
import org.bstone.util.direction.Direction;
import org.bstone.window.camera.CameraController;
import org.bstone.window.view.ScreenSpace;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class SceneShroomBase extends SceneBase implements LivingManager.OnLivingCreateListener<OldLivingEntity>, LivingManager.OnLivingDestroyListener<OldLivingEntity>
{
	protected CameraController cameraController;

	protected LivingManager<OldLivingEntity> livingManager;

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
		this.livingManager.onLivingCreate.addListener(this);
		this.livingManager.onLivingDestroy.addListener(this);

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

	public void spawn(OldLivingEntity living)
	{
		this.getLivingManager().add(living);
	}

	@Override
	public void onLivingCreate(OldLivingEntity living)
	{
		Entity entity = this.getEntityManager().createEntity(living);
		living.onEntityCreate(entity);
	}

	@Override
	public void onLivingDestroy(OldLivingEntity living)
	{
		Entity entity = this.getEntityManager().getEntityByComponent(living);
		entity.setDead();
		living.onEntityDestroy(entity);
	}

	@Override
	protected abstract Class<? extends RenderShroomBase> getRenderClass();

	@Override
	protected RenderShroomBase getRender()
	{
		return (RenderShroomBase) super.getRender();
	}

	public CameraController getMainCameraController()
	{
		return this.cameraController;
	}

	public Transform3c getMainCameraTransform()
	{
		return this.getRender().getMainCamera().transform();
	}

	public LivingManager<OldLivingEntity> getLivingManager()
	{
		return this.livingManager;
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
