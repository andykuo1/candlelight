package org.qsilver.render;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 4/30/17.
 */
public class RenderManager
{
	private Map<String, Render> renderMapping = new HashMap<>();

	public RenderManager()
	{
	}

	public void register(String id, Render render)
	{
		this.renderMapping.put(id, render);
	}

	public void remove(String id)
	{
		this.renderMapping.remove(id);
	}

	@SuppressWarnings("unchecked")
	public <T extends Render> T get(String id)
	{
		return (T) this.renderMapping.get(id);
	}
}
