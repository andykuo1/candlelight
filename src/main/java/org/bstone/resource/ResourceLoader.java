package org.bstone.resource;

import java.io.InputStream;

/**
 * Created by Andy on 10/26/17.
 */
public interface ResourceLoader<T extends AutoCloseable>
{
	T load(InputStream stream) throws Exception;

	default void unload(T resource) throws Exception
	{
		resource.close();
	}
}
