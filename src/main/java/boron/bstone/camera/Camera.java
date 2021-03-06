package boron.bstone.camera;

import boron.bstone.transform.Transform3;
import boron.bstone.transform.Transform3c;
import boron.bstone.util.listener.Listenable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Camera
{
	public interface OnCameraChangedListener
	{
		void onCameraChanged(Camera camera);
	}

	public final Listenable<OnCameraChangedListener> onCameraChanged = new Listenable<>((listener, objects) -> listener.onCameraChanged((Camera) objects[0]));

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
