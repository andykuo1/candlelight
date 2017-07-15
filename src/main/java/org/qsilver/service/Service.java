package org.qsilver.service;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class Service<T>
{
	protected abstract void onStart(T handler);
	protected abstract void onStop(T handler);
}
