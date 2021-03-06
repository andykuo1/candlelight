package apricot.stage_a.dood.component;

import apricot.stage_a.dood.entity.Component;

import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentMotion extends Component
{
	public final Vector3f motion = new Vector3f();
	public final Vector3f velocity = new Vector3f();
	public float friction = 0.88F;

	public final Vector3f maxVelocity = new Vector3f(1F, 1F, 1F);
	public float minVelocity = 0.01F;
}
