package net.jimboi.canary.stage_a.lantern.newglim.entity.component;

import net.jimboi.apricot.base.renderer.BillboardRenderer;

import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 6/14/17.
 */
public class EntityComponentBillboard implements EntityComponent
{
	public BillboardRenderer.Type billboardType;

	public EntityComponentBillboard(BillboardRenderer.Type type)
	{
		this.billboardType = type;
	}
}
