package apricot.stage_a.dood.component;

import apricot.stage_a.dood.entity.Component;

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
