package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.SceneBase;
import net.jimboi.boron.stage_a.shroom.component.LivingEntity;

import org.bstone.living.LivingManager;
import org.bstone.transform.Transform3c;
import org.zilar.BasicFirstPersonCameraController;
import org.zilar.bounding.BoundingManager;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class SceneShroomBase<R extends RenderShroomBase> extends SceneBase<R> implements LivingManager.OnLivingAddListener<LivingEntity>, LivingManager.OnLivingRemoveListener<LivingEntity>
{
	protected BasicFirstPersonCameraController cameraController;

	protected LivingManager<LivingEntity> livingManager;
	protected BoundingManager boundingManager;

	protected ShroomWorld world;

	protected abstract ShroomWorld createWorld();

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.cameraController = new BasicFirstPersonCameraController();

		this.livingManager = new LivingManager<>();
		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);

		this.boundingManager = new BoundingManager();

		this.world = this.createWorld();
	}

	@Override
	protected void onSceneStart()
	{
		this.cameraController.start(this.getRender().getMainCamera());

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

	public BasicFirstPersonCameraController getMainCameraController()
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

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}

	public ShroomWorld getWorld()
	{
		return this.world;
	}
}
