package net.jimboi.canary.stage_a.cuplet.scene_main.entity;

import net.jimboi.apricot.base.gui.GuiFrame;
import net.jimboi.apricot.base.gui.base.GuiManager;
import net.jimboi.canary.stage_a.cuplet.Cuplet;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 9/3/17.
 */
public class EntityVillager extends EntityHurtable
{
	private GuiFrame guiFrame;

	public EntityVillager(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.5F),
				world.createRenderable2D(transform, 'V', 0xFF00FF));
	}

	public void onInteract(EntityPlayer player)
	{
		System.out.println("BOOO");
		GuiManager guiManager = Cuplet.getCuplet().getRender().getGuiManager();
		this.guiFrame = new GuiFrame();
		this.guiFrame.setPosition(0, 0);
		this.guiFrame.setAlpha(1);
		this.guiFrame.setSize(20, 20);
		guiManager.addGui(this.guiFrame);
	}
}
