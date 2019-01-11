package boron.base.render;

/**
 * Created by Andy on 8/4/17.
 */
@Deprecated
public interface OldRenderHandler
{
	void onRenderLoad(OldRenderEngine renderEngine);
	void onRenderUnload(OldRenderEngine renderEngine);
	void onRenderUpdate(OldRenderEngine renderEngine, double delta);
}
