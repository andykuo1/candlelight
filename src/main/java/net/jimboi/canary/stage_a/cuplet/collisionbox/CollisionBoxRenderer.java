package net.jimboi.canary.stage_a.cuplet.collisionbox;

import net.jimboi.canary.stage_a.cuplet.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.canary.stage_a.cuplet.collisionbox.box.BoundingBox;
import net.jimboi.canary.stage_a.cuplet.collisionbox.box.GridBasedBoundingBox;
import net.jimboi.canary.stage_a.cuplet.collisionbox.collider.BoxCollider;
import net.jimboi.canary.stage_a.cuplet.renderer.MaterialProperty;
import net.jimboi.canary.stage_a.cuplet.renderer.WireframeRenderer;

import org.bstone.asset.Asset;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/8/17.
 */
public class CollisionBoxRenderer extends WireframeRenderer
{
	private final Asset<Mesh> quad;
	private final Material material;

	public CollisionBoxRenderer(Asset<Program> program, Asset<Mesh> quad)
	{
		super(program);

		this.quad = quad;
		this.material = new Material();
	}

	public void draw(Iterable<BoxCollider> colliders, int color)
	{
		Matrix4f matTransform = new Matrix4f();
		this.material.setProperty(MaterialProperty.COLOR, ColorUtil.getNormalizedRGBA(color, new Vector4f()));

		for(BoxCollider collider : colliders)
		{
			BoundingBox boundingBox = collider.getBoundingBox();
			if (boundingBox instanceof AxisAlignedBoundingBox)
			{
				AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) boundingBox;
				matTransform.identity().translation(aabb.getCenterX(), aabb.getCenterY(), 0).scale(aabb.getHalfWidth() * 2, aabb.getHalfHeight() * 2, 1);

				this.draw(this.quad, this.material, matTransform);
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
							this.draw(this.quad, this.material, matTransform);
						}
					}
				}
			}
		}
	}
}
