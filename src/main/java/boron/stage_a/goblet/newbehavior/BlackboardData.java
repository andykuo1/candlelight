package boron.stage_a.goblet.newbehavior;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 8/12/17.
 */
public class BlackboardData
{
	private final Map<String, Object> data = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <E> E get(String key)
	{
		return (E) this.data.get(key);
	}

	public Object set(String key, Object data)
	{
		return this.data.put(key, data);
	}

	public boolean contains(String key)
	{
		return this.data.containsKey(key);
	}
}
