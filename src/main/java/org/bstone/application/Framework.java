package org.bstone.application;

/**
 * Created by Andy on 10/11/17.
 */
public interface Framework
{
	default void onApplicationCreate(Application app) throws Exception
	{}

	void onApplicationStart(Application app);

	default void onApplicationUpdate(Application app)
	{}

	default void onApplicationStop(Application app)
	{}

	default void onApplicationDestroy(Application app)
	{}
}
