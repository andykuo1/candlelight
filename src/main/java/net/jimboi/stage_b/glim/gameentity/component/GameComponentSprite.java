package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.gnome.sprite.Sprite;
import net.jimboi.stage_b.gnome.sprite.SpriteSheet;

/**
 * Created by Andy on 6/13/17.
 */
public class GameComponentSprite extends GameComponent
{
	public SpriteSheet spritesheet;
	public Sprite sprite;

	public GameComponentSprite(SpriteSheet spritesheet)
	{
		this.spritesheet = spritesheet;
	}
}
