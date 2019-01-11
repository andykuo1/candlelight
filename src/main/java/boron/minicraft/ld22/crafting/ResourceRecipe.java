package boron.minicraft.ld22.crafting;

import boron.minicraft.ld22.entity.Player;
import boron.minicraft.ld22.item.ResourceItem;
import boron.minicraft.ld22.item.resource.Resource;

public class ResourceRecipe extends Recipe {
	private Resource resource;

	public ResourceRecipe(Resource resource) {
		super(new ResourceItem(resource, 1));
		this.resource = resource;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ResourceItem(resource, 1));
	}
}
