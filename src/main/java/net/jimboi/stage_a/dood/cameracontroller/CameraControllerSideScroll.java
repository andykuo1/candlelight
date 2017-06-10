package net.jimboi.stage_a.dood.cameracontroller;

import net.jimboi.base.Main;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.input.InputManager;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerSideScroll implements CameraController, Scene.OnSceneUpdateListener
{
	protected float speed = 50F;
	protected float distToTarget = 16F;

	protected Scene scene;
	protected Camera camera;

	protected float maxForward = 15F;
	protected float dampForward = 0.9F;
	protected float speedForward = 1F;

	protected float forward;
	protected float up;
	protected float right;

	protected Transform3 target;

	public CameraControllerSideScroll(Transform3 target)
	{
		this.target = target;
	}

	public CameraControllerSideScroll setSpeed(float speed)
	{
		this.speed = speed;
		return this;
	}

	public CameraControllerSideScroll setDistanceToTarget(float dist)
	{
		this.distToTarget = dist;
		this.maxForward = this.distToTarget - 1F;
		return this;
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		this.camera = camera;
		this.scene = Main.SCENE;
		this.scene.onSceneUpdate.addListener(this);
	}

	private static final Vector3f _VEC = new Vector3f();

	@Override
	public void onCameraUpdate(Camera camera, double delta)
	{
		if (InputManager.isInputDown("up")) this.forward -= this.speedForward;
		if (InputManager.isInputDown("down")) this.forward += this.speedForward;

		this.up = 0;
		if (InputManager.isInputDown("forward")) this.up += 1F;
		if (InputManager.isInputDown("backward")) this.up -= 1F;

		this.right = 0;
		if (InputManager.isInputDown("right")) this.right += 1F;
		if (InputManager.isInputDown("left")) this.right -= 1F;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		this.camera = null;
		this.scene.onSceneUpdate.deleteListener(this);
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		this.updateForward(delta);
		this.updateTargetPosition(delta);
		this.updateCameraPosition(delta);
	}

	protected void updateForward(double delta)
	{
		if (this.forward > this.maxForward)
		{
			this.forward = this.maxForward;
		}
		if (this.forward < -this.maxForward)
		{
			this.forward = -this.maxForward;
		}
		this.forward *= this.dampForward;
	}

	protected void updateTargetPosition(double delta)
	{
		this.target.position.x += this.right * this.speed;
		this.target.position.y += this.up * this.speed;
		this.target.position.z = this.distToTarget + this.forward;
	}

	protected void updateCameraPosition(double delta)
	{
		Vector3fc pos = this.target.position();
		this.camera.getTransform().setPosition(pos.x(), pos.y(), this.distToTarget + this.forward);
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public float getDistanceToTarget()
	{
		return this.distToTarget;
	}
}
