package net.jimboi.canary.stage_a.pong;

import net.jimboi.canary.stage_a.base.Model;

import org.bstone.entity.Component;

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
