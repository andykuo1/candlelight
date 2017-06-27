package org.qsilver.view;

import org.bstone.camera.Camera;
import org.bstone.input.InputManager;
import org.bstone.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

/**
 * Created by Andy on 6/18/17.
 */
public class MousePicker
{
	private final Camera camera;
	private final Window window;

	private final Matrix4f invertedProjection = new Matrix4f();
	private final Matrix4f invertedView = new Matrix4f();

	private final Vector3f mouseRay = new Vector3f();

	public MousePicker(Window window, Camera camera)
	{
		this.window = window;
		this.camera = camera;
	}

	public Vector3fc getMouseRay()
	{
		return this.mouseRay;
	}

	public void update()
	{
		this.camera.projection().invert(this.invertedProjection);
		this.camera.view().invert(this.invertedView);

		final float mouseX = InputManager.getInputAmount("mousex");
		final float mouseY = InputManager.getInputAmount("mousey");
		Vector2f mousePos = new Vector2f(mouseX, mouseY);

		Vector2f normalizedDeviceCoords = this.window.toNormalizedDeviceCoords(mousePos, mousePos);
		Vector4f clipCoords = new Vector4f(normalizedDeviceCoords.x, normalizedDeviceCoords.y, -1F, 1F);
		Vector4f viewCoords = this.toViewCoords(clipCoords, clipCoords);
		this.toWorldCoord(viewCoords, this.mouseRay);
	}

	private Vector4f toViewCoords(Vector4fc clipCoords, Vector4f dst)
	{
		this.invertedProjection.transform(clipCoords, dst);
		dst.z = -1F;
		dst.w = 0F;
		return dst;
	}

	private Vector3f toWorldCoord(Vector4fc viewCoords, Vector3f dst)
	{
		Vector4fc worldCoords = this.invertedView.transform(viewCoords, new Vector4f());
		return dst.set(worldCoords.x(), worldCoords.y(), worldCoords.z()).normalize();
	}
}
