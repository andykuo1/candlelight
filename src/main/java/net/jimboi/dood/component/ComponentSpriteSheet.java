package net.jimboi.dood.component;

import net.jimboi.mod.sprite.SpriteSheet;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/23/17.
 */
public class ComponentSpriteSheet extends Component
{
	public final String materialID;
	public SpriteSheet spriteSheet;

	public ComponentSpriteSheet(String materialID, SpriteSheet spriteSheet)
	{
		this.materialID = materialID;
		this.spriteSheet = spriteSheet;
	}
}
