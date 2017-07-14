package net.jimboi.stage_c.hoob.world;

import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_c.hoob.SceneHoob;
import net.jimboi.stage_c.hoob.world.agents.MoveAgent;
import net.jimboi.stage_c.hoob.world.agents.Town;
import net.jimboi.stage_c.hoob.world.agents.Traveller;
import net.jimboi.stage_c.hoob.world.agents.WorldAgent;

import org.bstone.input.InputManager;
import org.bstone.living.LivingManager;
import org.bstone.transform.Transform3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.view.MousePicker;
import org.zilar.base.GameEngine;

import java.util.Iterator;

/**
 * Created by Andy on 7/13/17.
 */
public class World implements LivingManager.OnLivingAddListener<WorldAgent>, LivingManager.OnLivingRemoveListener<WorldAgent>
{
	public final SceneHoob scene;
	public MousePicker picker;
	public Vector3f pickPos;

	public Traveller player;
	public LivingManager<WorldAgent> agents = new LivingManager<>();
	{
		this.agents.onLivingAdd.addListener(this);
		this.agents.onLivingRemove.addListener(this);
	}

	public World(SceneHoob scene)
	{
		this.scene = scene;
	}

	public void create()
	{
		this.picker = new MousePicker(GameEngine.WINDOW, GameEngine.INPUTENGINE, this.scene.getRenderer().getCamera());
		Entity pick = this.scene.spawnEntity(0, 0, 0, "crate", "quad", null);
		this.pickPos = pick.getComponent(EntityComponentTransform.class).transform.position;
		this.pickPos.z = 0.1F;

		Town town = this.spawnTown(0, 0);
		this.spawnTown(8, -5);
		this.player = this.spawnTraveller(1, 1);
		this.player.moveSpeed = 4F;
	}

	public void update(double delta)
	{
		Vector3fc camera = this.scene.getRenderer().getCamera().transform().position3();
		this.pickPos.x = camera.x();
		this.pickPos.y = camera.y();

		Vector3fc mouseRay = this.picker.getMouseRay();
		if (mouseRay.z() < 0)
		{
			float z = camera.z();
			while (z > 0)
			{
				this.pickPos.x += mouseRay.x();
				this.pickPos.y += mouseRay.y();
				z += mouseRay.z();
			}
			this.pickPos.x -= mouseRay.x() * (1 - z);
			this.pickPos.y -= mouseRay.y() * (1 - z);
		}

		//TODO: This is a hack to make it look like it does...
		this.pickPos.x += mouseRay.x();
		this.pickPos.y += mouseRay.y();
		//this.pickPos.z = 0;

		if (InputManager.isInputDown("mouseleft"))
		{
			this.player.target.x = this.pickPos.x;
			this.player.target.y = this.pickPos.y;
		}

		this.agents.update(delta);

		this.updateEntities();
	}

	private void updateEntities()
	{
		Iterator<WorldAgent> iter = this.agents.getLivingIterator();
		while(iter.hasNext())
		{
			WorldAgent agent = iter.next();
			Entity entity = this.scene.getEntityManager().getEntityByComponent(agent);

			Transform3 transform = entity.getComponent(EntityComponentTransform.class).transform;
			transform.position.x = agent.pos.x();
			transform.position.y = agent.pos.y();
		}
	}

	public <T extends WorldAgent> T spawnAgent(T agent)
	{
		this.agents.add(agent);
		return agent;
	}

	public void destroyAgent(WorldAgent agent)
	{
		agent.setDead();
	}

	public LivingManager<WorldAgent> getAgents()
	{
		return this.agents;
	}

	public Town spawnTown(float x, float y)
	{
		Town town = this.spawnAgent(new Town(this));
		town.pos.x = x;
		town.pos.y = y;
		return town;
	}

	public Traveller spawnTraveller(float x, float y)
	{
		Traveller traveller = this.spawnAgent(new Traveller(this));
		traveller.pos.x = x;
		traveller.pos.y = y;
		return traveller;
	}

	@Override
	public void onLivingAdd(WorldAgent agent)
	{
		boolean motion = agent instanceof MoveAgent;
		Entity entity = this.scene.spawnEntity(agent.pos.x, agent.pos.y, 0, "bunny", "quad", motion ? "bunny" : null);
		entity.addComponent(agent);
	}

	@Override
	public void onLivingRemove(WorldAgent agent)
	{
		this.scene.getEntityManager().getEntityByComponent(agent).setDead();
	}
}
