package net.jimboi.mod.asset;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 4/27/17.
 */
public class AutoCloseableGroup implements AutoCloseable
{
	private Set<AutoCloseable> closeables = new HashSet<>();

	public AutoCloseableGroup open(AutoCloseable... closeables)
	{
		for(AutoCloseable closeable : closeables)
		{
			this.closeables.add(closeable);
		}

		return this;
	}

	@Override
	public void close()
	{
		for(AutoCloseable closeable : this.closeables)
		{
			try
			{
				closeable.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		this.closeables.clear();
	}
}
