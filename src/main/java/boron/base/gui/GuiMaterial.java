package boron.base.gui;

import boron.base.gui.base.Gui;
import boron.base.material.OldMaterial;

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
