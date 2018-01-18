package org.bstone.application;

import org.bstone.kernel.Kernel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for any application.
 */
public class Application extends Kernel
{
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

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
			LOG.info("Creating Application...");
			this.framework.onApplicationCreate(this);
			LOG.info("Running Application...");
			super.run();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			LOG.info("Destroying Application...");
			this.framework.onApplicationDestroy(this);
		}
	}

	@Override
	protected void onKernelStart()
	{
		LOG.info("Starting Application...");
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
		LOG.info("Stopping Application...");
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
