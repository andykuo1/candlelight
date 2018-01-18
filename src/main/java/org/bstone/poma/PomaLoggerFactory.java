package org.bstone.poma;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 1/18/18.
 */
public class PomaLoggerFactory implements ILoggerFactory
{
	private final Map<String, PomaLogger> loggerMap;

	public PomaLoggerFactory()
	{
		this.loggerMap = new HashMap<>();
	}

	@Override
	public Logger getLogger(String name)
	{
		synchronized (this.loggerMap)
		{
			if (!this.loggerMap.containsKey(name))
			{
				this.loggerMap.put(name, new PomaLogger(name));
			}
		}
		return this.loggerMap.get(name);
	}
}
