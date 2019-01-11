package apricot.base.scene;

import apricot.base.service.Service;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class SceneService extends Service<Scene>
{
	public SceneService(Scene scene)
	{
		super(scene.getSceneServices());
	}

	protected abstract void onSceneUpdate(Scene scene);
}
