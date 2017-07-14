package net.jimboi.stage_b.glim.entity.system;

import net.jimboi.stage_b.glim.entity.component.EntityComponentBillboard;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.camera.Camera;
import org.joml.Vector3f;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.transform.Transform;
import org.zilar.renderer.BillboardRenderer;
import org.zilar.transform.Transform3Quat;

import java.util.Collection;

/**
 * Created by Andy on 6/14/17.
 */
public class EntitySystemBillboard extends AbstractUpdateableSystem
{
	private Camera camera;

	public EntitySystemBillboard(EntityManager entityManager, Camera camera)
	{
		super(entityManager);

		this.camera = camera;
	}

	private static Vector3f VEC = new Vector3f();
	private static Vector3f UP = new Vector3f();

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(EntityComponentBillboard.class, EntityComponentTransform.class);
		for(Entity entity : entities)
		{
			EntityComponentBillboard componentBillboard = entity.getComponent(EntityComponentBillboard.class);
			EntityComponentTransform componentTransform = entity.getComponent(EntityComponentTransform.class);

			Transform3Quat transform = (Transform3Quat) componentTransform.transform;

			this.camera.getTransform().position().sub(transform.position, VEC);

			if (componentBillboard.billboardType == BillboardRenderer.Type.SPHERICAL)
			{
				throw new UnsupportedOperationException("Spherical billboards are not yet supported!");
			}
			else
			{
				transform.rotation.rotationXYZ(0, (float) -Math.atan2(VEC.z(), VEC.x()) + Transform.HALF_PI, 0);
			}
		}
	}
}
