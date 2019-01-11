package boron.stage_a.goblet.gui;

import boron.base.material.OldMaterial;
import boron.stage_a.goblet.gui.base.Gui;

/**
 * Created by Andy on 7/17/17.
 */
public class GuiMaterial extends Gui
{
	public OldMaterial material;

	public GuiMaterial(OldMaterial material)
	{
		this.material = material;
	}

	@Override
	public boolean isSelectable(boolean active)
	{
		return false;
	}
}
