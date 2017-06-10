package net.jimboi.glim.gameentity.component;

import net.jimboi.glim.bounding.Bounding;

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
