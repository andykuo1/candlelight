package net.jimboi.dood;

import net.jimboi.base.Main;
import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.dood.system.EntitySystem;
import net.jimboi.dood.system.SystemAnimatedTexture;
import net.jimboi.dood.system.SystemBox2D;
import net.jimboi.dood.system.SystemInstance;
import net.jimboi.dood.system.SystemMotion;
import net.jimboi.mod.Light;

import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.bstone.util.MathUtil;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.qsilver.entity.Entity;

/**
 * Created by Andy on 5/21/17.
 */
public class SceneDood extends SceneDoodBase implements InputEngine.OnInputUpdateListener
{
	private SystemInstance systemInstance;
	private SystemMotion systemMotion;
	private SystemBox2D systemBox2D;
	private SystemAnimatedTexture systemAnimatedTexture;

	private Entity entityPlayer;

	public SceneDood()
	{
		super(new RendererDood());

		Main.INPUTENGINE.onInputUpdate.addListener(this);
	}

	private Light directionLight;

	@Override
	protected void onSceneCreate()
	{
		//Add Entities Here
		ResourcesDood.INSTANCE.lights.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 0.1F, 0));
		ResourcesDood.INSTANCE.lights.add(this.directionLight = Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

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

		ResourcesDood.INSTANCE.camera.setCameraController(new CameraControllerDood(this.entityPlayer.getComponent(ComponentTransform.class), this.entityPlayer.getComponent(ComponentBox2DBody.class)));
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		Light light = ResourcesDood.INSTANCE.lights.get(0);
		light.position = new Vector4f(ResourcesDood.INSTANCE.camera.getTransform().position, 1);
		light.coneDirection = ResourcesDood.INSTANCE.camera.getTransform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	private Entity grabbedEntity;
	private Joint grabbedJoint;

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		ResourcesDood.INSTANCE.camera.update(inputEngine);

		if (this.entityPlayer == null) return;

		Body playerBody = this.entityPlayer.getComponent(ComponentBox2DBody.class).getBody();

		if (InputManager.isInputDown("action"))
		{
			if (grabbedEntity == null)
			{
				Vector3fc from = this.entityPlayer.getComponent(ComponentTransform.class).transform.position();
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
	protected void onSceneDestroy()
	{
		this.systemInstance.stop();
		this.systemMotion.stop();
		this.systemBox2D.stop();
		this.systemAnimatedTexture.stop();
		EntitySystem.stopAll();
	}
}
