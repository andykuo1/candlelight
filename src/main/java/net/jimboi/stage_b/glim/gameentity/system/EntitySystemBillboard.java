package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.gameentity.component.GameComponentBillboard;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.glim.renderer.BillboardRenderer;
import net.jimboi.stage_b.gnome.transform.Transform;
import net.jimboi.stage_b.gnome.transform.Transform3Quat;

import org.bstone.camera.Camera;
import org.joml.Vector3f;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 6/14/17.
 */
public class EntitySystemBillboard extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	private Camera camera;

	public EntitySystemBillboard(EntityManager entityManager, Scene scene, Camera camera)
	{
		super(entityManager);

		this.registerListenable(scene.onSceneUpdate);

		this.camera = camera;
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	private static Vector3f VEC = new Vector3f();
	private static Vector3f UP = new Vector3f();

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(GameComponentBillboard.class, GameComponentTransform.class);
		for(Entity entity : entities)
		{
			GameComponentBillboard componentBillboard = entity.getComponent(GameComponentBillboard.class);
			GameComponentTransform componentTransform = entity.getComponent(GameComponentTransform.class);

			Transform3Quat transform = (Transform3Quat) componentTransform.transform;

			this.camera.getTransform().position.sub(transform.position, VEC);

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
