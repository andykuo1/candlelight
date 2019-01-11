package canary.bstone.service;

/**
 * Created by Andy on 11/2/17.
 */
public interface Service<E>
{
	void start(E handler);
	void stop(E handler);

	default void pause(E handler) {}
	default void resume(E handler) {}

}
