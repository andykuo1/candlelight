package net.jimboi.boron.stage_a.candle;

import net.jimboi.boron.base.SceneLivingBase;
import net.jimboi.boron.stage_a.candle.entity.EntityCandleBase;
import net.jimboi.boron.stage_a.candle.world.WorldCandle;

import org.bstone.window.camera.Camera;
import org.qsilver.render.RenderService;

/**
 * Created by Andy on 7/29/17.
 */
public class SceneCandle extends SceneLivingBase<EntityCandleBase>
{
	private WorldCandle world;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.world = new WorldCandle(this);
	}

	@Override
	protected void onSceneStart()
	{
		this.startService(this.world);
	}

	@Override
	protected void onSceneStop()
	{
		super.onSceneStop();

		this.stopService(this.world);
	}

	public WorldCandle getWorld()
	{
		return this.world;
	}

	public Camera getMainCamera()
	{
		return this.getRender().getMainCamera();
	}

	@Override
	protected Class<? extends RenderService> getRenderClass()
	{
		return CandleRenderer.class;
	}

	@Override
	protected CandleRenderer getRender()
	{
		return (CandleRenderer) super.getRender();
	}
}
