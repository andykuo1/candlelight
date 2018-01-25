package org.zilar.in2;

import org.bstone.kernel.Engine;
import org.bstone.newinput.device.event.InputEvent;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Andy on 1/24/18.
 */
public class InputEngine implements Engine
{
	private final Queue<InputEvent> inputEvents = new ArrayBlockingQueue<>(100);

	private InputManager inputManager;

	public InputEngine()
	{
	}

	public InputEngine setInputManager(InputManager inputManager)
	{
		this.inputManager = inputManager;
		return this;
	}

	@Override
	public boolean initialize()
	{
		if (this.inputManager == null)
			throw new IllegalStateException("must designate input manager before initialization");

		return true;
	}

	@Override
	public void update()
	{
		this.inputManager.process(this.inputEvents.iterator());
	}

	@Override
	public void terminate()
	{
		this.inputManager.clear();
	}

	public void queueEvent(InputEvent evt)
	{
		this.inputEvents.offer(evt);
	}

	public InputEvent nextEvent()
	{
		return this.inputEvents.poll();
	}

	public boolean hasNextEvent()
	{
		return this.inputEvents.isEmpty();
	}

	public InputManager getInputManager()
	{
		return this.inputManager;
	}
}
