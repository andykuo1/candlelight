package net.jimboi.stage_c.gui;

import net.jimboi.stage_c.gui.base.Gui;

import org.bstone.material.Material;

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
