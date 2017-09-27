package net.jimboi.apricot.base.gui;

import net.jimboi.apricot.base.gui.base.Gui;
import net.jimboi.apricot.base.material.OldMaterial;

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
