package net.jimboi.apricot.stage_a.mod.scene;

import net.jimboi.apricot.stage_a.blob.livings.LivingBase;

import org.bstone.living.Living;
import org.bstone.living.LivingManager;
import org.qsilver.scene.Scene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class LivingSceneBase extends Scene
{
	protected final LivingManager<Living> livingManager;
	protected final Set<LivingBase> entities = new HashSet<>();

	public LivingSceneBase()
	{
		this.livingManager = new LivingManager<>();
		this.livingManager.onLivingCreate.addListener((living -> {
			if (living instanceof LivingBase)
			{
				this.entities.add((LivingBase) living);
			}
		}));
		this.livingManager.onLivingDestroy.addListener((living -> {
			if (living instanceof LivingBase)
			{
				this.entities.remove(living);
			}
		}));
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.livingManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.livingManager.destroy();
	}

	public LivingManager<Living> getLivingManager()
	{
		return this.livingManager;
	}

	public Iterator<LivingBase> getEntities()
	{
		return this.entities.iterator();
	}
}
