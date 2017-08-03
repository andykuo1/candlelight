package net.jimboi.apricot.stage_a.dood;

import net.jimboi.apricot.stage_a.base.OldMain;
import net.jimboi.apricot.stage_a.dood.component.ComponentBox2DBody;
import net.jimboi.apricot.stage_a.dood.component.ComponentTransform;
import net.jimboi.apricot.stage_a.dood.entity.Entity;
import net.jimboi.apricot.stage_a.dood.system.EntitySystem;
import net.jimboi.apricot.stage_a.dood.system.SystemAnimatedTexture;
import net.jimboi.apricot.stage_a.dood.system.SystemBox2D;
import net.jimboi.apricot.stage_a.dood.system.SystemInstance;
import net.jimboi.apricot.stage_a.dood.system.SystemMotion;
import net.jimboi.apricot.stage_a.mod.ModLight;

import org.bstone.input.InputEngine;
import org.bstone.input.InputLayer;
import org.bstone.input.InputManager;
import org.bstone.window.camera.CameraController;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 5/21/17.
 */
public class SceneDood extends SceneDoodBase implements InputLayer
{
	private CameraController cameraController;

	private SystemInstance systemInstance;
	private SystemMotion systemMotion;
	private SystemBox2D systemBox2D;
	private SystemAnimatedTexture systemAnimatedTexture;

	private Entity entityPlayer;

	public SceneDood()
	{
		super(new RendererDood());
	}

	private ModLight directionLight;

	@Override
	protected void onSceneCreate()
	{
		OldMain.INPUTENGINE.addInputLayer(this);

		//Add Entities Here
		ResourcesDood.INSTANCE.lights.add(ModLight.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 0.1F, 0));
		ResourcesDood.INSTANCE.lights.add(this.directionLight = ModLight.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		this.systemInstance = new SystemInstance(this.entityManager, ((RendererDood) this.renderer).getInstanceManager());
		this.systemMotion = new SystemMotion(this.entityManager, this);
		this.systemBox2D = new SystemBox2D(this.entityManager, this);
		this.systemAnimatedTexture = new SystemAnimatedTexture(this.entityManager, this, this.renderer);

		this.systemInstance.start();
		this.systemMotion.start();
		this.systemBox2D.start();
		this.systemAnimatedTexture.start();
	}

	@Override
	protected void onSceneStart()
	{
		this.entityPlayer = EntityPlayer.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityHills.create(this.entityManager, ResourcesDood.INSTANCE.hills);

		this.cameraController = new CameraControllerDood(this.entityPlayer.getComponent(ComponentTransform.class), this.entityPlayer.getComponent(ComponentBox2DBody.class));
		this.cameraController.start(ResourcesDood.INSTANCE.camera);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		ModLight light = ResourcesDood.INSTANCE.lights.get(0);
		light.position = new Vector4f(ResourcesDood.INSTANCE.camera.transform().position3(), 1);
		light.coneDirection = ResourcesDood.INSTANCE.camera.transform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	private Entity grabbedEntity;
	private Joint grabbedJoint;

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.entityPlayer == null) return;

		this.cameraController.update(1);

		Body playerBody = this.entityPlayer.getComponent(ComponentBox2DBody.class).getBody();

		if (InputManager.isInputDown("action"))
		{
			if (grabbedEntity == null)
			{
				Vector3fc from = this.entityPlayer.getComponent(ComponentTransform.class).transform.position3();
				float dist = 0;
				Body nearest = null;
				Body body = this.systemBox2D.getWorld().getBodyList();
				while (body != null)
				{
					if (body != playerBody && body.getType() == BodyType.DYNAMIC)
					{
						float f = from.distance(body.getPosition().x, body.getPosition().y, 0);
						if (nearest == null)
						{
							nearest = body;
							dist = f;
						}
						else
						{
							if (f < dist)
							{
								nearest = body;
								dist = f;
							}
						}
					}
					body = body.getNext();
				}

				if (nearest != null && dist < 2F)
				{
					this.grabbedEntity = this.systemBox2D.getEntityFromBody(nearest);
					if (this.grabbedEntity != null)
					{
						RevoluteJointDef def = new RevoluteJointDef();
						def.bodyA = playerBody;
						def.bodyB = nearest;
						def.collideConnected = false;
						float dir = MathUtil.sign(playerBody.getPosition().x - nearest.getPosition().x);
						def.localAnchorA.set(-dir, 0);
						def.localAnchorB.set(0, 0);
						this.grabbedJoint = this.systemBox2D.getWorld().createJoint(def);
					}
				}
			}
		}
		else
		{
			if (this.grabbedEntity != null)
			{
				this.systemBox2D.getWorld().destroyJoint(this.grabbedJoint);
				this.grabbedJoint = null;
				this.grabbedEntity = null;
			}
		}
	}

	@Override
	protected void onSceneStop()
	{
		super.onSceneStop();
		this.cameraController.stop();
	}

	@Override
	protected void onSceneDestroy()
	{
		OldMain.INPUTENGINE.removeInputLayer(this);
		this.systemInstance.stop();
		this.systemMotion.stop();
		this.systemBox2D.stop();
		this.systemAnimatedTexture.stop();
		EntitySystem.stopAll();
	}
}
