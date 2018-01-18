package org.bstone.poma.motivator;

/**
 * Created by Andy on 1/18/18.
 */
class Heart implements Runnable
{
	private final Poma self;

	private volatile boolean heartbeat;

	Heart(Poma self)
	{
		this.self = self;
	}

	@Override
	public void run()
	{
		this.heartbeat = true;
		this.self.onStart();

		while (this.isAlive())
		{
			try
			{
				this.doHeartBeat();
			}
			catch (Exception e)
			{
				this.self.onHeartSkip();
				continue;
			}
			this.self.onHeartBeat();
		}

		this.self.onDeath();
	}

	public void doHeartBeat() throws Exception
	{
		Thread.sleep(this.getHeartRate());
	}

	public int getHeartRate()
	{
		return 5000;
	}

	public boolean isAlive()
	{
		return this.heartbeat;
	}

	public void kill()
	{
		this.heartbeat = false;
	}
}
