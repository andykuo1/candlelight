package net.jimboi.apricot.stage_b.glim.entity.system;

import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBillboard;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.bstone.window.camera.Camera;
import org.joml.Vector3f;
import org.zilar.entity.AbstractUpdateableEntitySystem;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityManager;
import org.zilar.renderer.BillboardRenderer;

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