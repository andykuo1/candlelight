package canary.bstone.render;

/**
 * Created by Andy on 1/17/18.
 */
public interface RenderFramework
{
	default void load() {}
	default void unload() {}

	void render();
}
