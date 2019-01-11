package canary.bstone.scheduler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 11/29/17.
 */
public class Scheduler
{
	protected final Queue<Runnable> tasks = new LinkedBlockingQueue<>();

	public void addScheduledTask(Runnable runnable)
	{
		this.tasks.offer(runnable);
	}

	public void process()
	{
		while(!this.tasks.isEmpty())
		{
			Runnable task = this.tasks.poll();
			task.run();
		}
	}

	public void clear()
	{
		this.tasks.clear();
	}
}
