package net.jimboi.test.multiuser.nettypacket;

/**
 * Created by Andy on 8/15/17.
 */
public abstract class Command
{
	public abstract boolean process(String[] args);
}
