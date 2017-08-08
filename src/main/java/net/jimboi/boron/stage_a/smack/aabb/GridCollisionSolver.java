package net.jimboi.boron.stage_a.smack.aabb;

import java.util.function.Consumer;

/**
 * Created by Andy on 8/7/17.
 */
public class GridCollisionSolver
{
	public static void checkCollision(BoxCollider collider, GridCollider other, Consumer<BoxCollisionData> onCollision)
	{
		AxisAlignedBoundingBox box = collider.getBoundingBox();
		GridBasedBoundingMap obox = other.getGridBox();
		float offsetX = box.centerX - obox.offsetX;
		float offsetY = box.centerY - obox.offsetY;

		if (offsetX < 0 || offsetY < 0 || offsetX >= obox.width || offsetY >= obox.height) return;

		int fromX = Math.max((int) Math.floor(offsetX - box.halfWidth), 0);
		int fromY = Math.max((int) Math.floor(offsetY - box.halfHeight), 0);
		int toX = Math.min((int) Math.ceil(offsetX + box.halfWidth), obox.width);
		int toY = Math.min((int) Math.ceil(offsetY + box.halfHeight), obox.height);

		GridAlignedBoundingBox gridAlignedBox = other.getBoundingBox();
		for(int x = fromX; x < toX; ++x)
		{
			for (int y = fromY; y < toY; ++y)
			{
				if (obox.isSolid(x + y * obox.width))
				{
					gridAlignedBox.setOffset(x, y);
					BoxCollisionData data = BoxCollisionSolver.solve(box, gridAlignedBox);
					if (data != null)
					{
						data.collider = other;
						onCollision.accept(data);
					}
				}
			}
		}
	}
}
