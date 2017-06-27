package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.glim.renderer.BillboardRenderer;

/**
 * Created by Andy on 6/14/17.
 */
public class GameComponentBillboard extends GameComponent
{
	public BillboardRenderer.Type billboardType;

	public GameComponentBillboard(BillboardRenderer.Type type)
	{
		this.billboardType = type;
	}
}
