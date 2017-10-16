package net.jimboi.test.message;

/**
 * Created by Andy on 10/11/17.
 */
@FunctionalInterface
public interface MessageHandler
{
	boolean onProcessMessageEvent(Message message);
}
