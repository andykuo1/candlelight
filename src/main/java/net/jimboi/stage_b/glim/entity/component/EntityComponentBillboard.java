package net.jimboi.stage_b.glim.entity.component;

import org.zilar.entity.EntityComponent;
import org.zilar.renderer.BillboardRenderer;

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
