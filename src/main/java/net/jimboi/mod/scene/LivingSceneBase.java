package net.jimboi.mod.scene;

import net.jimboi.blob.livings.LivingBase;

import org.qsilver.living.LivingManager;
import org.qsilver.scene.Scene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class LivingSceneBase extends Scene
{
	protected final LivingManager livingManager;
	protected final Set<LivingBase> entities = new HashSet<>();

	public LivingSceneBase()
	{
		this.livingManager = new LivingManager();

		this.livingManager.addLivingSet(this.entities, LivingBase.class);
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		this.livingManager.update(delta);
	}

	@Override
	public void onSceneStop()
	{
		this.livingManager.destroyAll();
	}

	public LivingManager getLivingManager()
	{
		return this.livingManager;
	}

	public Iterator<LivingBase> getEntities()
	{
		return this.entities.iterator();
	}
}
