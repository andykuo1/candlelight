package net.jimboi.dood.component;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentInstanceable extends Component
{
	public final String modelID;
	public final String materialID;
	public final String renderType;

	public ComponentInstanceable(String modelID, String materialID, String renderType)
	{
		this.modelID = modelID;
		this.materialID = materialID;
		this.renderType = renderType;
	}
}
