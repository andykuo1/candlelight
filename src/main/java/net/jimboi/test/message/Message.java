package net.jimboi.test.message;

/**
 * Created by Andy on 10/3/17.
 */
public class Message
{
	public final MessageHandler src;
	public final MessageHandler dst;
	public final String type;
	public final Object data;

	public Message(MessageHandler src, MessageHandler dst, String type, Object data)
	{
		this.src = src;
		this.dst = dst;
		this.type = type;
		this.data = data;
	}
}
