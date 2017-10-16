package net.jimboi.test.message;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Andy on 10/12/17.
 */
public class Messenger
{
	private final Queue<Message> messages = new PriorityBlockingQueue<>(11,
			Comparator.comparing(Message::getPriority));

	private final Set<MessageHandler> handlers = new HashSet<>();

	private final Thread thread;

	public Messenger()
	{
		this.thread = Thread.currentThread();
	}

	public void subscribe(MessageHandler handler)
	{
		this.handlers.add(handler);
	}

	public void unsubscribe(MessageHandler handler)
	{
		this.handlers.remove(handler);
	}

	public void publish(Message message)
	{
		this.messages.offer(message);
	}

	public void poll(double maxDelta)
	{
		double delta = 0;
		long start = System.currentTimeMillis();
		while(delta < maxDelta)
		{
			this.processEvent(this.messages.poll());
			delta = System.currentTimeMillis() - start;
		}
	}

	protected void processEvent(Message message)
	{
		if (Thread.currentThread() != this.thread)
		{
			throw new IllegalStateException("must be called on the same thread!");
		}

		for(MessageHandler handler : this.handlers)
		{
			if (handler.onProcessMessageEvent(message))
			{
				break;
			}
		}
	}

	public void clear()
	{
		this.messages.clear();
		this.handlers.clear();
	}
}
