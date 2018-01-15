package org.bstone.tick;

/**
 * Created by Andy on 1/15/18.
 */
public class FrameCounter extends TickService
{
	long timeCounter = 0;
	@Override
	protected void onFirstUpdate(TickEngine tickEngine)
	{

	}

	@Override
	protected void onLastUpdate(TickEngine tickEngine)
	{

	}

	@Override
	protected void onEarlyUpdate()
	{

	}

	@Override
	protected void onFixedUpdate()
	{
		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;

			System.out.print("[");
			{
				//System.out.print("UPS: " + this.tickEngine.getUpdateCounter().get());
				System.out.print(" || ");
				//System.out.print("FPS: " + this.renderEngine.getFrameCounter().get());
			}
			System.out.println("]");
		}
	}

	@Override
	protected void onLateUpdate()
	{

	}
}
