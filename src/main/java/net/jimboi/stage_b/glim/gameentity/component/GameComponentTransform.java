package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.gnome.transform.Transform;

/**
 * Created by Andy on 6/1/17.
 */
public class GameComponentTransform extends GameComponent
{
	public final Transform transform;

	public GameComponentTransform(Transform transform)
	{
		this.transform = transform;
	}

}
