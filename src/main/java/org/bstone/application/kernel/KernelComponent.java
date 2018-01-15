package org.bstone.application.kernel;


/**
 * The base component of a {@link Kernel}. Use
 * {@link Kernel#startEngine(Engine)} to appropriately initialize the
 * engine and listen for update events. If certain engines are dependent on
 * others, start the parent first; the application will maintain order of engine
 * initializations and terminations.
 */
public final class KernelComponent
{
	private Engine engine;
	private boolean dead;

	public KernelComponent(Engine engine)
	{
		this.engine = engine;
		this.dead = false;
	}

	/**
	 * Called when the engine is started
	 *
	 * @return whether it successfully started
	 */
	protected boolean onStart()
	{
		return this.engine.initialize();
	}

	/**
	 * Called each loop by kernel in starting order
	 */
	protected void onUpdate()
	{
		this.engine.update();
	}

	/**
	 * Called when the engine is stopped
	 */
	protected void onStop()
	{
		this.engine.terminate();
	}

	/**
	 * Mark the engine for destruction; will not continue to update after this loop
	 */
	public final void setDead()
	{
		this.dead = true;
	}

	/**
	 * Whether the engine is destroyed yet
	 */
	public final boolean isDead()
	{
		return this.dead;
	}

	public Engine getEngine()
	{
		return this.engine;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof KernelComponent && this.engine.equals(((KernelComponent) o).engine);
	}
}
