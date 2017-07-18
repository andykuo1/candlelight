package net.jimboi.stage_c.hoob.world;

import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_c.hoob.SceneHoob;
import net.jimboi.stage_c.hoob.world.agents.MoveAgent;
import net.jimboi.stage_c.hoob.world.agents.Town;
import net.jimboi.stage_c.hoob.world.agents.Traveller;
import net.jimboi.stage_c.hoob.world.agents.WorldAgent;

import org.bstone.input.InputEngine;
import org.bstone.input.InputLayer;
import org.bstone.input.InputManager;
import org.bstone.living.LivingManager;
import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.view.ScreenSpace;
import org.joml.Vector3f;
import org.zilar.base.GameEngine;
import org.zilar.bounding.ShapeAABB;
import org.zilar.entity.Entity;

import java.util.Iterator;

/**
 * Created by Andy on 7/13/17.
 */
public class World implements LivingManager.OnLivingAddListener<WorldAgent>, LivingManager.OnLivingRemoveListener<WorldAgent>, InputLayer
{
	public final SceneHoob scene;
	public Vector3f pickPos;

	public Traveller player;
	public LivingManager<WorldAgent> agents = new LivingManager<>();
	{
		this.agents.onLivingAdd.addListener(this);
		this.agents.onLivingRemove.addListener(this);
	}

	private ScreenSpace screenSpace;

	public World(SceneHoob scene)
	{
		this.scene = scene;
		GameEngine.INPUTENGINE.addInputLayer(this);
	}

	public void create()
	{
		Entity pick = this.scene.spawnEntity(0, 0, 0, "crate", "quad");
		this.pickPos = pick.getComponent(EntityComponentTransform.class).transform.position;
		this.pickPos.z = 0.1F;

		this.spawnTown(2, 2);
		this.spawnTown(8, -5);
		this.player = this.spawnTraveller(0, 0);
		this.player.moveSpeed = 4F;

		this.screenSpace = new ScreenSpace(GameEngine.WINDOW.getCurrentViewPort(), this.scene.getRenderer().getCamera(), Direction.CENTER, true, false);
	}

	private boolean actionMove = false;

	public void update(double delta)
	{
		float mouseX = InputManager.getInputAmount("mousex");
		float mouseY = InputManager.getInputAmount("mousey");
		this.screenSpace.getPoint2DFromScreen(mouseX, mouseY, this.pickPos);

		if (this.actionMove)
		{
			this.player.target.x = this.pickPos.x;
			this.player.target.y = this.pickPos.y;
		}

		this.agents.update(delta);

		this.updateEntities();
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.actionMove = InputManager.isInputDown("mouseleft"))
		{
			InputManager.consumeInput("mouseleft");
		}
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
		Entity entity = this.scene.spawnEntity(agent.pos.x, agent.pos.y, 0, "bunny", "quad", motion, agent.isSolid() ? new ShapeAABB(agent.pos.x(), agent.pos.y(), agent.getSize()) : null, motion);
		entity.addComponent(agent);
	}

	@Override
	public void onLivingRemove(WorldAgent agent)
	{
		this.scene.getEntityManager().getEntityByComponent(agent).setDead();
	}
}
