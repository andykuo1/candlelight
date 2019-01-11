package apricot.stage_b.glim.entity.system;

import apricot.base.entity.AbstractUpdateableEntitySystem;
import apricot.base.entity.Entity;
import apricot.base.entity.EntityManager;
import apricot.base.renderer.BillboardRenderer;
import apricot.stage_b.glim.entity.component.EntityComponentBillboard;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;

import apricot.bstone.camera.Camera;
import apricot.bstone.transform.Transform;
import apricot.bstone.transform.Transform3;
import org.joml.Vector3f;

import java.util.Collection;

/**
 * Created by Andy on 6/14/17.
 */
public class EntitySystemBillboard extends AbstractUpdateableEntitySystem
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

			Transform3 transform = componentTransform.transform;

			this.camera.transform().position3().sub(transform.position, VEC);

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
