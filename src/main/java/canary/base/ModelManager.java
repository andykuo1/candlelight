package canary.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 12/2/17.
 */
public class ModelManager
{
	private Map<String, Model> models = new HashMap<>();

	public void registerModel(String name, Model model)
	{
		this.models.put(name, model);
	}

	public void unregisterModel(String name)
	{
		this.models.remove(name);
	}

	public Model getModel(String name)
	{
		return this.models.get(name);
	}
}
