package org.bstone.network.handler;

/**
 * Created by Andy on 12/1/17.
 */
public class IntegratedServer extends Server
{
	private final Client client;

	public IntegratedServer(Client client)
	{
		this.client = client;
	}
}
