package org.zilar.gui;

import net.jimboi.apricot.base.material.OldMaterial;

import org.zilar.gui.base.Gui;

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
