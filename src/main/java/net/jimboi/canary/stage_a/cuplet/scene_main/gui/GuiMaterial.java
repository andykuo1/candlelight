package net.jimboi.canary.stage_a.cuplet.scene_main.gui;

import net.jimboi.apricot.base.material.OldMaterial;
import net.jimboi.canary.stage_a.cuplet.scene_main.gui.base.Gui;

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
