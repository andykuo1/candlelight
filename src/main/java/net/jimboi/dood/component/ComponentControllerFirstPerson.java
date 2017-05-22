package net.jimboi.dood.component;

import net.jimboi.mod.transform.Transform;

import org.bstone.camera.Camera;
import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentControllerFirstPerson extends Component
{
	public static final float MAX_PITCH = Transform.ANG2RAD * 80F;

	public final Camera camera;

	public float pitch;
	public float yaw;

	public float sensitivity = 0.01F;
	public boolean mouseLock = false;

	public float forward;
	public float up;
	public float right;

	public ComponentControllerFirstPerson(Camera camera)
	{
		this.camera = camera;
	}
}
