package net.jimboi.stage_b.glim.gameentity;

import net.jimboi.stage_b.glim.gameentity.component.GameComponent;

import org.bstone.component.EntityManifest;

/**
 * Created by Andy on 6/8/17.
 */
public class GameEntity extends EntityManifest<GameComponent>
{
	private boolean dead = false;

	GameEntity() {}

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}

	@Override
	public EntityManifest addComponent(GameComponent component)
	{
		return super.addComponent(component);
	}

	@Override
	public <T extends GameComponent> T removeComponent(Class<T> componentType)
	{
		return super.removeComponent(componentType);
	}
}
