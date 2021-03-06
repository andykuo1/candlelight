package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Created by Andy on 1/18/18.
 */
public class StaticLoggerBinder implements LoggerFactoryBinder
{
	/**
	 * The unique instance of this class.
	 *
	 */
	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	/**
	 * Return the singleton of this class.
	 *
	 * @return the StaticLoggerBinder singleton
	 */
	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	/**
	 * Declare the version of the SLF4J API this implementation is compiled against.
	 * The value of this field is modified with each major release.
	 */
	// to avoid constant folding by the compiler, this field must *not* be final
	public static String REQUESTED_API_VERSION = "1.7.25"; // !final

	private static final String loggerFactoryClassStr = canary.bstone.poma.PomaLoggerFactory.class.getName();

	/**
	 * The ILoggerFactory instance returned by the {@link #getLoggerFactory}
	 * method should always be the same object
	 */
	private final ILoggerFactory loggerFactory;

	private StaticLoggerBinder() {
		loggerFactory = new canary.bstone.poma.PomaLoggerFactory();
	}

	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	public String getLoggerFactoryClassStr() {
		return loggerFactoryClassStr;
	}
}
