package net.jimboi.boron.stage_a.goblet;

import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.goblet.entity.EntityPlayer;

import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.render.material.PropertyTexture;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.joml.Vector3fc;
import org.qsilver.asset.Asset;

import java.util.Iterator;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletWorld
{
	private final GobletEntityManager entityManager;

	private GobletCameraController cameraController;

	private EntityPlayer player;

	public GobletWorld()
	{
		this.entityManager = new GobletEntityManager();
	}

	public void start()
	{
		this.cameraController = new GobletCameraController();
		this.cameraController.start(Goblet.getGoblet().getRender().getCamera());

		Transform3 transform = new Transform3();
		this.player = new EntityPlayer(this, transform);
		this.spawnEntity(this.player);
		this.cameraController.setTarget(transform);
	}

	public void stop()
	{
		this.cameraController.stop();

		this.entityManager.destroy();
	}

	public void update()
	{
		this.cameraController.update(1);
		this.entityManager.update();
	}

	public void spawnEntity(GobletEntity entity)
	{
		this.entityManager.spawn(entity);
	}

	public Transform3 createTransform(Transform3c transform)
	{
		Transform3 result = new Transform3();
		result.position.set(transform.position3());
		result.rotation.set(transform.quaternion());
		result.scale.set(transform.scale3());
		return result;
	}

	public AxisAlignedBoundingBox createBoundingBox(Transform3c transform, float size)
	{
		final Vector3fc pos = transform.position3();
		return new AxisAlignedBoundingBox(pos.x(), pos.y(), size / 2F, size / 2F);
	}

	public EntityComponentRenderable createRenderable2D(Transform3 transform, char c, int color)
	{
		return new EntityComponentRenderable(transform, this.createModel2D(c, color));
	}

	public Model createModel2D(char c, int color)
	{
		return new Model(Asset.wrap(Goblet.getGoblet().getRender().mshQuad), this.createMaterial2D(c, color));
	}

	public Material createMaterial2D(char c, int color)
	{
		Material material = new Material();
		PropertyTexture.addProperty(material);
		PropertyColor.addProperty(material);

		PropertyTexture.setSprite(material, Goblet.getGoblet().getRender().fontSheet.get(c));
		PropertyColor.setColor(material, color);
		return material;
	}

	@SuppressWarnings("unchecked")
	public <T extends GobletEntity> T getNearestEntity(float x, float y, Class<? extends T> entity)
	{
		GobletEntity nearest = null;
		float distance = -1F;
		Iterator<GobletEntity> livings = this.entityManager.getLivings().getLivingIterator();
		while(livings.hasNext())
		{
			GobletEntity living = livings.next();
			if (entity.isAssignableFrom(living.getClass()))
			{
				float f = living.getTransform().position3().distanceSquared(x, y, 0);
				if (nearest == null || f < distance)
				{
					nearest = living;
					distance = f;
				}
			}
		}
		return (T) nearest;
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	public GobletEntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public CollisionBoxManager getBoundingManager()
	{
		return this.entityManager.getBoundings();
	}
}
