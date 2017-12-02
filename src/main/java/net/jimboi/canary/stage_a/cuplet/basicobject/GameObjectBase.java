package net.jimboi.canary.stage_a.cuplet.basicobject;

import org.bstone.gameobject.GameObject;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/12/17.
 */
public class GameObjectBase extends GameObject
{
	protected final Transform3 transform;
	protected final ComponentRenderable renderable;

	public GameObjectBase(Transform3 transform, ComponentRenderable renderable)
	{
		this.transform = transform;
		this.renderable = renderable;
	}

	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentTransform3(this.transform));
		this.addComponent(this.renderable);
	}

	public final ComponentRenderable getRenderable()
	{
		return this.renderable;
	}

	public final Transform3 getTransform()
	{
		return this.transform;
	}
}
