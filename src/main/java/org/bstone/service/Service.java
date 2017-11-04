package org.bstone.service;

/**
 * Created by Andy on 11/2/17.
 */
public interface Service<E>
{
	void start(E handler);
	void stop(E handler);
}
