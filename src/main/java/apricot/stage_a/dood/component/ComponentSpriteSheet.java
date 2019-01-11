package apricot.stage_a.dood.component;

import apricot.stage_a.dood.entity.Component;
import apricot.stage_a.mod.sprite.SpriteSheet;

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
