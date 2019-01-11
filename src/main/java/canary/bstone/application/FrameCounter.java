package canary.bstone.application;

import canary.bstone.util.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andy on 1/15/18.
 */
public class FrameCounter
{
	private static final Logger LOG = LoggerFactory.getLogger(FrameCounter.class);

	private final Counter updates;
	private final Counter frames;
	private long time = 0;

	public FrameCounter()
	{
		this.updates = new Counter();
		this.frames = new Counter();

		this.time = System.currentTimeMillis();
		this.updates.reset();
		this.frames.reset();
	}

	public void poll()
	{
		if (System.currentTimeMillis() - this.time > 1000)
		{
			this.time += 1000;

			LOG.info("(" + this.updates.poll() + "/" + this.frames.poll() + ")fps");
		}
	}

	public void tick()
	{
		this.updates.next();
	}

	public void frame()
	{
		this.frames.next();
	}

	public final Counter getUpdates()
	{
		return this.updates;
	}

	public final Counter getFrames()
	{
		return this.frames;
	}
}
