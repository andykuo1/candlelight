package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.gnome.sprite.SpriteSheet;

/**
 * Created by Andy on 6/13/17.
 */
public class GameComponentSprite extends GameComponent
{
	public SpriteSheet spritesheet;
	public float frametime;
	public float framedelta;

	public GameComponentSprite(SpriteSheet spritesheet, float frametime)
	{
		this.spritesheet = spritesheet;
		this.frametime = frametime;
		this.framedelta = 0;
	}
}
