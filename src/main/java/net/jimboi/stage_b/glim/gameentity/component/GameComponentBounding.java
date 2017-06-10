package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.glim.bounding.Bounding;

/**
 * Created by Andy on 6/4/17.
 */
public class GameComponentBounding extends GameComponent
{
	public Bounding bounding;

	public GameComponentBounding(Bounding bounding)
	{
		this.bounding = bounding;
	}
}
