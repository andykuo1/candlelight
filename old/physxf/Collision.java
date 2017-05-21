package net.jimboi.physx;

/**
 * Created by Andy on 5/17/17.
 */
public class Collision
{
	public static void resolveCollision(RigidBody a, RigidBody b)
	{
		int rvelX = b.velocityX - a.velocityX;
		int rvelY = b.velocityY - a.velocityY;

		int normalX = 0;
		int normalY = 0;

		int nvel = Mathf.dotProduct(rvelX, rvelY, normalX, normalY);

		if (nvel > 0) return;

		int e = Mathf.min(a.restitution, b.restitution);

		int j = Mathf.mul(-(Mathf.F_ONE + e), nvel);
		j = Mathf.div(j, a.inv_mass + b.inv_mass);

		int impulseX = Mathf.mul(j, normalX);
		int impulseY = Mathf.mul(j, normalY);

		int mass_sum = a.mass + b.mass;

		int ratio = Mathf.div(a.mass, mass_sum);
		a.velocityX -= Mathf.mul(ratio, impulseX);
		a.velocityY -= Mathf.mul(ratio, impulseY);

		ratio = Mathf.div(b.mass, mass_sum);
		b.velocityX += Mathf.mul(ratio, impulseX);
		b.velocityY += Mathf.mul(ratio, impulseY);
	}

	public static void correctPosition(RigidBody a, RigidBody b)
	{
		int percent = Mathf.div(Mathf.F_ONE, Mathf.toFixedFloat(5));
		int slop = Mathf.div(Mathf.F_ONE, Mathf.toFixedFloat(100));

		int normalX = 0;
		int normalY = 0;
		int penetration = 0;

		int inv_mass_sum = a.inv_mass + b.inv_mass;
		int penetration_depth = Mathf.max(penetration - slop, 0);
		int corrRatio = Mathf.mul(Mathf.div(penetration_depth, inv_mass_sum), percent);

		int corrX = Mathf.mul(corrRatio, normalX);
		int corrY = Mathf.mul(corrRatio, normalY);

		a.posX -= a.inv_mass * corrX;
		a.posY -= a.inv_mass * corrY;

		b.posX += b.inv_mass * corrX;
		b.posY += b.inv_mass * corrY;
	}

	public static boolean CirclevsCircle(Manifold manifold)
	{
		RigidBody a = manifold.a;
		RigidBody b = manifold.b;

		int nX = b.posX - a.posX;
		int nY = b.posY - a.posY;

		int tX = b.posX - a.posX;
		int tY = b.posY - a.posY;

		int r = a.circle.radius + b.circle.radius;
		r = Mathf.mul(r, r);

		if (Mathf.lenSqu(nX, nY) > r) return false;

		int d = Mathf.len(nX, nY);
		if (d != 0)
		{
			manifold.penetration = r - d;
			manifold.normalX = Mathf.div(tX, d);
			manifold.normalY = Mathf.div(tY, d);
			return true;
		}
		else
		{
			manifold.penetration = a.circle.radius;
			manifold.normalX = Mathf.toFixedFloat(1);
			manifold.normalY = 0;
			return true;
		}
	}

	public static boolean AABBvsAABB(Manifold manifold)
	{
		RigidBody a = manifold.a;
		RigidBody b = manifold.b;

		int nX = b.posX - a.posX;
		int nY = b.posY - a.posY;

		AABB abox = a.aabb;
		AABB bbox = b.aabb;

		int abox_difX = abox.maxX - abox.minX;
		int bbox_difX = bbox.maxX - bbox.minX;
		int a_extX = Mathf.div(abox_difX, Mathf.toFixedFloat(2));
		int b_extX = Mathf.div(bbox_difX, Mathf.toFixedFloat(2));

		int x_overlap = a_extX + b_extX - Mathf.abs(nX);

		if (x_overlap > 0)
		{
			int abox_difY = abox.maxY - abox.minY;
			int bbox_difY = bbox.maxY - bbox.minY;
			int a_extY = Mathf.div(abox_difY, Mathf.toFixedFloat(2));
			int b_extY = Mathf.div(bbox_difY, Mathf.toFixedFloat(2));

			int y_overlap = a_extY + b_extY - Mathf.abs(nY);

			if (y_overlap > 0)
			{
				if (x_overlap > y_overlap)
				{
					if (nX < 0)
					{
						manifold.normalX = -Mathf.F_ONE;
						manifold.normalY = 0;
					}
					else
					{
						manifold.normalX = 0;
						manifold.normalY = 0;
						manifold.penetration = x_overlap;
						return true;
					}
				}
				else
				{
					if (nY < 0)
					{
						manifold.normalX = 0;
						manifold.normalY = -Mathf.F_ONE;
					}
					else
					{
						manifold.normalX = 0;
						manifold.normalY = 1;
						manifold.penetration = y_overlap;
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean AABBvsCircle(Manifold manifold)
	{
		RigidBody a = manifold.a;
		RigidBody b = manifold.b;

		int nX = b.posX - a.posX;
		int nY = b.posY - a.posY;

		int closeX = nX;
		int closeY = nY;

		int x_extent = Mathf.div(a.aabb.maxX - a.aabb.minX, Mathf.toFixedFloat(2));
		int y_extent = Mathf.div(a.aabb.maxY - a.aabb.minY, Mathf.toFixedFloat(2));

		closeX = Mathf.clamp(-x_extent, x_extent, closeX);
		closeY = Mathf.clamp(-y_extent, y_extent, closeY);

		boolean inside = false;

		if (nX == closeX && nY == closeY)
		{
			inside = true;

			if (Mathf.abs(nX) > Mathf.abs(nY))
			{
				if (closeX > 0)
					closeX = x_extent;
				else
					closeX = -x_extent;
			}
			else
			{
				if (closeY > 0)
					closeY = y_extent;
				else
					closeY = -y_extent;
			}
		}

		int normalX = nX - closeX;
		int normalY = nY - closeY;
		int d = Mathf.len(normalX, normalY);
		int r = b.circle.radius;

		if (d > r && !inside)
		{
			return false;
		}

		d = Mathf.sqrt(d);

		if (inside)
		{
			manifold.normalX = -nX;
			manifold.normalY = -nY;
			manifold.penetration = r - d;
		}
		else
		{
			manifold.normalX = nX;
			manifold.normalY = nY;
			manifold.penetration = r - d;
		}

		return true;
	}
}
