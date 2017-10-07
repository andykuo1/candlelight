package net.jimboi.test.message;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 10/4/17.
 */
public class MessageManager
{
	private final Queue<Message> messages = new LinkedBlockingQueue<>();

	public void dispatch(double maxDelta)
	{
		double delta = 0;
		double now = System.currentTimeMillis();
		while (delta < maxDelta && !this.messages.isEmpty())
		{
			final Message message = this.messages.poll();

			this.processImmediately(message);

			double next = System.currentTimeMillis();
			delta += next - now;
			now = next;
		}
	}

	public void dispatchAll()
	{
		while(!this.messages.isEmpty())
		{
			final Message message = this.messages.poll();

			this.processImmediately(message);
		}
	}

	public void process(Message message)
	{
		this.messages.offer(message);
	}

	public void processImmediately(Message message)
	{
		synchronized (message.dst)
		{
			message.dst.onMessageProcess(message);
		}
	}
}
