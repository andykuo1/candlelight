package org.bstone.window.view;

import org.bstone.util.direction.Direction;
import org.bstone.window.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by Andy on 7/16/17.
 */
public class ScreenSpace
{
	protected final ViewPort viewport;
	protected final Camera camera;

	protected final Direction centerPoint;
	protected final boolean leftToRight;
	protected final boolean topToBottom;

	public ScreenSpace(ViewPort viewport, Camera camera, Direction centerPoint, boolean leftToRight, boolean topToBottom)
	{
		this.viewport = viewport;
		this.camera = camera;

		this.centerPoint = centerPoint;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
	}

	private static final Matrix4f MAT4 = new Matrix4f();
	private static final Vector2f VEC2 = new Vector2f();
	private static final Vector3f VEC3A = new Vector3f();
	private static final Vector3f VEC3B = new Vector3f();

	public Vector3f getPoint2DFromScreen(float screenX, float screenY, Vector3f dst)
	{
		Matrix4fc invertedViewProjection = this.getInvertedViewProjectionMatrix(MAT4);
		Vector2fc screen = this.getScreenOffset(screenX, screenY, VEC2);

		Vector3f near = unproject(invertedViewProjection, this.viewport, screen.x(), screen.y(), 0, VEC3A);
		Vector3f far = unproject(invertedViewProjection, this.viewport, screen.x(), screen.y(), 1, VEC3B);

		float f = (0 - near.z) / (far.z - near.z);
		screenX = (near.x + f * (far.x - near.x));
		screenY = (near.y + f * (far.y - near.y));
		return dst.set(screenX, screenY, 0);
	}

	public Vector3f getPointFromScreen(float screenX, float screenY, float depth, Vector3f dst)
	{
		Matrix4fc invertedViewProjection = this.getInvertedViewProjectionMatrix(MAT4);
		Vector2fc screen = this.getScreenOffset(screenX, screenY, VEC2);

		return unproject(invertedViewProjection, this.viewport, screen.x(), screen.y(), depth, dst);
	}

	public Vector3f getVectorFromScreen(float screenX, float screenY, Vector3f dst)
	{
		Matrix4fc invertedViewProjection = this.getInvertedViewProjectionMatrix(MAT4);
		Vector2fc screen = this.getScreenOffset(screenX, screenY, VEC2);

		unproject(invertedViewProjection, this.viewport, screen.x(), screen.y(), 0, VEC3A);
		unproject(invertedViewProjection, this.viewport, screen.x(), screen.y(), 1, VEC3B);

		return dst.set(VEC3B).sub(VEC3A).normalize();
	}

	protected Matrix4f getInvertedViewProjectionMatrix(Matrix4f dst)
	{
		Matrix4fc view = this.camera.view();
		Matrix4fc projection = this.camera.projection();
		return projection.mul(view, dst).invert();
	}

	protected Vector2f getScreenOffset(float screenX, float screenY, Vector2f dst)
	{
		final float w = this.viewport.getWidth();
		final float h = this.viewport.getHeight();
		final float w2 = w / 2F;
		final float h2 = h / 2F;

		switch (this.centerPoint)
		{
			case EAST:
				screenX += -w2;
				screenY += 0;
				break;
			case NORTHEAST:
				screenX += -w2;
				screenY += h2;
				break;
			case NORTH:
				screenX += 0;
				screenY += h2;
				break;
			case NORTHWEST:
				screenX += w2;
				screenY += h2;
				break;
			case WEST:
				screenX += w2;
				screenY += 0;
				break;
			case SOUTHWEST:
				screenX += w2;
				screenY += -h2;
				break;
			case SOUTH:
				screenX += 0;
				screenY += -h2;
				break;
			case SOUTHEAST:
				screenX += -w2;
				screenY += -h2;
				break;
			case CENTER:
			default:
				break;
		}

		if (!this.leftToRight) screenX = w - screenX;
		if (!this.topToBottom) screenY = h - screenY;

		return dst.set(screenX, screenY);
	}

	public final ViewPort getViewport()
	{
		return this.viewport;
	}

	public final Camera getCamera()
	{
		return this.camera;
	}

	public final boolean isFormat(Direction centerPoint, boolean leftToRight, boolean topToBottom)
	{
		return this.centerPoint == centerPoint && this.leftToRight == leftToRight && this.topToBottom == topToBottom;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ScreenSpace)
		{
			ScreenSpace screenSpace = (ScreenSpace) o;
			return this.equalsImpl(screenSpace.viewport, screenSpace.camera, screenSpace.centerPoint, screenSpace.leftToRight, screenSpace.topToBottom);
		}

		return false;
	}

	private boolean equalsImpl(ViewPort viewport, Camera camera, Direction centerPoint, boolean leftToRight, boolean topToBottom)
	{
		return this.viewport.equals(viewport) && this.camera.equals(camera) && this.isFormat(centerPoint, leftToRight, topToBottom);
	}


	public static ScreenSpace derive(ScreenSpace screenSpace, Direction centerPoint, boolean leftToRight, boolean topToBottom)
	{
		return new ScreenSpace(screenSpace.viewport, screenSpace.camera, centerPoint, leftToRight, topToBottom);
	}

	public static ScreenSpace deriveOpenGLFormat(ScreenSpace screenSpace)
	{
		return new ScreenSpace(screenSpace.viewport, screenSpace.camera, Direction.CENTER, true, false);
	}

	public static Vector3f unproject(Matrix4fc invertedViewProjection, ViewPort viewport, float screenX, float screenY, float z, Vector3f dst)
	{
		Vector4f normalizedDeviceCoords = new Vector4f();
		normalizedDeviceCoords.x = (screenX - viewport.getX()) / viewport.getWidth() * 2.0F - 1.0F;
		normalizedDeviceCoords.y = (screenY - viewport.getY()) / viewport.getHeight() * 2.0F - 1.0F;
		normalizedDeviceCoords.z = 2.0F * z - 1.0F;
		normalizedDeviceCoords.w = 1.0F;

		Vector4f objectCoords = invertedViewProjection.transform(normalizedDeviceCoords);
		if (objectCoords.w != 0.0F) objectCoords.w = 1.0F / objectCoords.w;

		return dst.set(objectCoords.x, objectCoords.y, objectCoords.z).mul(objectCoords.w);
	}
}
