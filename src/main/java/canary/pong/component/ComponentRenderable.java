package canary.pong.component;

import canary.base.Model;

import canary.bstone.entity.Component;

/**
 * Created by Andy on 12/2/17.
 */
public class ComponentRenderable implements Component
{
	public String modelName;
	public Model customModel;

	public ComponentRenderable(String modelName)
	{
		this.modelName = modelName;
		this.customModel = null;
	}

	public ComponentRenderable setCustomModel(Model model)
	{
		this.customModel = model;
		return this;
	}
}
