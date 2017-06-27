package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.IntersectionData;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentHeading;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentNavigator;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTargeter;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.astar.map.NavigatorCardinalMap;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;
import org.qsilver.transform.Transform;
import org.qsilver.util.MathUtil;
import org.zilar.instance.InstanceManager;
import org.zilar.transform.Transform3Quat;

import java.util.Collection;

/**
 * Created by Andy on 6/14/17.
 */
public class EntitySystemHeading extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	private BoundingManager boundingManager;
	private InstanceManager instanceManager;

	public EntitySystemHeading(EntityManager entityManager, Scene scene, BoundingManager boundingManager, InstanceManager instanceManager)
	{
		super(entityManager);
		this.registerListenable(scene.onSceneUpdate);

		this.boundingManager = boundingManager;
		this.instanceManager = instanceManager;
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	private static final Vector3f VEC = new Vector3f();

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(GameComponentHeading.class, GameComponentTransform.class, GameComponentBounding.class);
		for(Entity entity : entities)
		{
			GameComponentTransform componentTransform = entity.getComponent(GameComponentTransform.class);
			GameComponentHeading componentHeading = entity.getComponent(GameComponentHeading.class);
			GameComponentBounding componentBounding = entity.getComponent(GameComponentBounding.class);
			Transform3Quat transform = (Transform3Quat) componentTransform.transform;

			//Update navigator
			if (entity.hasComponent(GameComponentNavigator.class) && entity.hasComponent(GameComponentTargeter.class))
			{
				GameComponentNavigator componentNavigator = entity.getComponent(GameComponentNavigator.class);
				GameComponentTargeter componentTargeter = entity.getComponent(GameComponentTargeter.class);
				if (componentNavigator.tracks == null || componentNavigator.tracks.isEmpty())
				{
					Vector3fc startpos = transform.position;
					Vector3fc endpos = componentNavigator.world.getRandomTilePos(false);
					if (endpos == null) break;

					NavigatorCardinalMap.Cell start = new NavigatorCardinalMap.Cell((int) startpos.x(), (int) startpos.z());
					NavigatorCardinalMap.Cell end = new NavigatorCardinalMap.Cell((int) endpos.x(), (int) endpos.z());

					componentNavigator.tracks = componentNavigator.navigator.navigate(start, end);
				}
				else if (componentTargeter.target == null)
				{
					NavigatorCardinalMap.Cell cell = componentNavigator.tracks.pop();
					componentTargeter.target = new Vector3f(cell.x + 0.5F, 0, cell.y + 0.5F);
				}
			}

			//Update target
			if (entity.hasComponent(GameComponentTargeter.class))
			{
				GameComponentTargeter componentTargeter = entity.getComponent(GameComponentTargeter.class);
				if (componentTargeter.target == null) break;

				VEC.set(transform.position);
				VEC.y = 0;
				if (!componentTargeter.isNearTarget(VEC))
				{
					transform.position().sub(componentTargeter.target, VEC);
					componentHeading.moveDirection(VEC);
				}
				else
				{
					componentHeading.stop();
					componentTargeter.target = null;
				}
			}

			//Update motion
			if (componentHeading.motion != 0)
			{
				componentHeading.velocity += componentHeading.speed * componentHeading.motion;
				if (componentHeading.velocity > componentHeading.maxVelocity)
				{
					componentHeading.velocity = componentHeading.maxVelocity;
				}
			}
			else
			{
				componentHeading.velocity *= componentHeading.friction;
			}

			//Rotating to heading
			while(componentHeading.heading - componentHeading.rotation > Transform.PI)
			{
				componentHeading.heading -= Transform.PI2;
			}
			while(componentHeading.heading - componentHeading.rotation < -Transform.PI)
			{
				componentHeading.heading += Transform.PI2;
			}
			componentHeading.rotation = MathUtil.lerp(componentHeading.rotation, componentHeading.heading, (float) delta * componentHeading.rotspeed);

			//Moving towards heading
			float magnitude = (float) delta * componentHeading.velocity;

			if (magnitude > 0)
			{
				VEC.set((float) Math.cos(componentHeading.rotation), 0, (float) Math.sin(componentHeading.rotation)).normalize().mul(magnitude);
				IntersectionData data = this.boundingManager.checkIntersection(componentBounding.bounding, VEC);
				if (data != null)
				{
					transform.translate(data.delta.x(), data.delta.y(), data.delta.z());
				}
				else
				{
					transform.translate(VEC.x(), VEC.y(), VEC.z());
				}
			}
		}
	}
}
