package canary.test.conworld.item;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.world.World;

/**
 * Created by Andy on 8/30/17.
 */
public interface IUseable
{
	void onUseItem(World world, Actor owner);

	boolean isUseable(World world, Actor owner);
}
