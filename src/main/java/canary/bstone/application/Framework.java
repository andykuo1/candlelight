package canary.bstone.application;

/**
 * The basis of an Application; describes the relationship between engines and
 * further client-specific operations.
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
