package org.bstone.application;

import org.bstone.application.kernel.Kernel;

/**
 * Main entry point for any application.
 */
public class Application extends Kernel
{
	public static Application createApplication()
	{
		return new Application();
	}

	private Framework framework;

	/**
	 * Prepares the application with the client-defined framework
	 *
	 * @param framework the listening framework
	 */
	public Application setFramework(Framework framework)
	{
		if (this.isRunning()) throw new IllegalStateException("cannot change framework while running");

		this.framework = framework;
		return this;
	}

	public void startOnCurrentThread()
	{
		this.run();
	}

	/**
	 * Start running the Application on the current thread
	 */
	@Override
	public void run()
	{
		try
		{
			System.out.println("Creating Application...");
			this.framework.onApplicationCreate(this);
			System.out.println("Running Application...");
			super.run();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Destroying Application...");
			this.framework.onApplicationDestroy(this);
		}
	}

	@Override
	protected void onKernelStart()
	{
		System.out.println("Starting Application...");
		this.framework.onApplicationStart(this);
	}

	@Override
	protected void onKernelUpdate()
	{
		this.framework.onApplicationUpdate(this);
	}

	@Override
	protected void onKernelStop()
	{
		System.out.println("Stopping Application...");
		this.framework.onApplicationStop(this);
	}

	public Thread getApplicationThread()
	{
		return this.getKernelThread();
	}

	public Framework getFramework()
	{
		return this.framework;
	}
}
