package net.jimboi.boron.stage_a.base.collisionbox;

import net.jimboi.boron.base_ab.render.renderer.WireframeProgramRenderer;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.box.BoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.box.GridBasedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;

import org.bstone.mogli.Mesh;
import org.bstone.util.ColorUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 8/8/17.
 */
public class CollisionBoxRenderer extends WireframeProgramRenderer
{
	private final Mesh quad;

	public CollisionBoxRenderer()
	{
		super();

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
		this.quad = ModelUtil.createStaticMesh(mb.bake(false, true));
		mb.clear();
	}

	@Override
	public void close() throws Exception
	{
		this.quad.close();

		super.close();
	}

	public void draw(Iterable<BoxCollider> colliders, int color)
	{
		Matrix4f matTransform = new Matrix4f();
		Vector3fc vecColor = ColorUtil.getNormalizedRGB(color, new Vector3f());

		for(BoxCollider collider : colliders)
		{
			BoundingBox boundingBox = collider.getBoundingBox();
			if (boundingBox instanceof AxisAlignedBoundingBox)
			{
				AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) boundingBox;
				matTransform.identity().translation(aabb.getCenterX(), aabb.getCenterY(), 0).scale(aabb.getHalfWidth() * 2, aabb.getHalfHeight() * 2, 1);
				this.draw(this.quad, vecColor, matTransform);
			}
			else if (boundingBox instanceof GridBasedBoundingBox)
			{
				GridBasedBoundingBox gbbb = (GridBasedBoundingBox) boundingBox;
				for(int i = gbbb.getWidth() - 1; i >= 0; --i)
				{
					for(int j = gbbb.getHeight() - 1; j >= 0; --j)
					{
						float x = gbbb.getOffsetX() + i;
						float y = gbbb.getOffsetY() + j;
						if (gbbb.isSolid(x, y))
						{
							matTransform.identity().translation(x + 0.5F, y + 0.5F, 0);
							this.draw(this.quad, vecColor, matTransform);
						}
					}
				}
			}
		}
	}
}
