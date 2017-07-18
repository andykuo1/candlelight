package org.zilar.gui;

import org.bstone.material.Material;
import org.zilar.gui.base.Gui;

/**
 * Created by Andy on 7/17/17.
 */
public class GuiMaterial extends Gui
{
	public Material material;

	public GuiMaterial(Material material)
	{
		this.material = material;
	}

	@Override
	public boolean isSelectable(boolean active)
	{
		return false;
	}
}
