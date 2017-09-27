package net.jimboi.apricot.stage_b.glim.entity.system;

import net.jimboi.apricot.base.astar.map.NavigatorCardinalMap;
import net.jimboi.apricot.stage_b.glim.bounding.BoundingManager;
import net.jimboi.apricot.stage_b.glim.bounding.IntersectionData;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentHeading;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentNavigator;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTargeter;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.util.MathUtil;
import org.zilar.entity.AbstractUpdateableEntitySystem;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityManager;

import java.util.Collection;

/**
 * Created by Andy on 6/14/17.
 */
public class EntitySystemHeading extends AbstractUpdateableEntitySystem
{
	private BoundingManager boundingManager;

	public EntitySystemHeading(EntityManager entityManager, BoundingManager boundingManager)
	{
		super(entityManager);

		this.boundingManager = boundingManager;
	}

	private static final Vector3f VEC = new Vector3f();

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(EntityComponentHeading.class, EntityComponentTransform.class, EntityComponentBounding.class);
		for(Entity entity : entities)
		{
			EntityComponentTransform componentTransform = entity.getComponent(EntityComponentTransform.class);
			EntityComponentHeading componentHeading = entity.getComponent(EntityComponentHeading.class);
			EntityComponentBounding componentBounding = entity.getComponent(EntityComponentBounding.class);
			Transform3 transform = (Transform3) componentTransform.transform;

			//Update navigator
			if (entity.hasComponent(EntityComponentNavigator.class) && entity.hasComponent(EntityComponentTargeter.class))
			{
				EntityComponentNavigator componentNavigator = entity.getComponent(EntityComponentNavigator.class);
				EntityComponentTargeter componentTargeter = entity.getComponent(EntityComponentTargeter.class);
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
			if (entity.hasComponent(EntityComponentTargeter.class))
			{
				EntityComponentTargeter componentTargeter = entity.getComponent(EntityComponentTargeter.class);
				if (componentTargeter.target == null) break;

				VEC.set(transform.position);
				VEC.y = 0;
				if (!componentTargeter.isNearTarget(VEC))
				{
					transform.position3().sub(componentTargeter.target, VEC);
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
