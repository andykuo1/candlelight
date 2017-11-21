package org.bstone.application;

/**
 * The base component of an {@link Application}. Use
 * {@link Application#startEngine(Engine)} to appropriately initialize
 * the engine and listen for update events. If certain engines are dependent on
 * others, start the parent first; the application will maintain order of
 * engine initializations and terminations.
 */
public abstract class Engine
{
	private boolean dead = false;

	/**
	 * Called when the engine is started
	 *
	 * @param app the application the engine is operating under
	 */
	protected abstract boolean onStart(Application app);

	/**
	 * Called each loop by the application in starting order
	 *
	 * @param app the application the engine is operating under
	 */
	protected abstract void onUpdate(Application app);

	/**
	 * Called when the engine is stopped
	 *
	 * @param app the application the engine is operating under
	 */
	protected abstract void onStop(Application app);

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
}
