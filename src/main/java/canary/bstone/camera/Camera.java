package canary.bstone.camera;

import canary.bstone.transform.Transform3;
import canary.bstone.transform.Transform3c;
import canary.bstone.util.listener.Listenable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/**
 * The viewport representation of the rendered space in a window. It contains the view, projection, and transformation matrices that describe the viewport in the world.
 *
 * Created by Andy on 5/19/17.
 */
public abstract class Camera
{
	public final Listenable onCameraChanged = new Listenable(this);

	private boolean dirty;

	protected final Transform3 transform;
	protected final Matrix4f viewMatrix = new Matrix4f();
	protected final Matrix4f rotationMatrix = new Matrix4f();
	protected final Matrix4f projectionMatrix = new Matrix4f();

	public Camera(Transform3 transform)
	{
		this.transform = transform;
		this.dirty = true;
	}

	public final Transform3c transform()
	{
		return this.transform;
	}

	public final Matrix4fc orientation()
	{
		if (this.dirty) this.updateCamera();
		return this.rotationMatrix;
	}

	public final Matrix4fc projection()
	{
		if (this.dirty) this.updateCamera();
		return this.projectionMatrix;
	}

	public final Matrix4fc view()
	{
		if (this.dirty) this.updateCamera();
		return this.viewMatrix;
	}

	private void updateCamera()
	{
		this.updateProjectionMatrix(this.projectionMatrix.identity());
		this.updateViewMatrix(this.viewMatrix.identity());
		this.updateRotationMatrix(this.rotationMatrix.identity());

		this.dirty = false;
		this.onCameraChanged.notifyListeners(this);
	}

	protected abstract void updateProjectionMatrix(Matrix4f mat);

	protected abstract void updateRotationMatrix(Matrix4f mat);

	protected abstract void updateViewMatrix(Matrix4f mat);

	public final boolean isDirty()
	{
		return this.dirty;
	}

	public final void markDirty()
	{
		this.dirty = true;
	}
}
