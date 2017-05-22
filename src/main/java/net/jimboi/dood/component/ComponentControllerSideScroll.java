package net.jimboi.dood.component;

import org.bstone.camera.Camera;
import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentControllerSideScroll extends Component
{
	public final Camera camera;

	public float up;
	public float right;

	public ComponentControllerSideScroll(Camera camera)
	{
		this.camera = camera;
	}
}
