package net.jimboi.apricot.stage_c.hoob.world;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.base.living.OldLivingManager;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.apricot.stage_c.hoob.SceneHoob;
import net.jimboi.apricot.stage_c.hoob.world.agents.MoveAgent;
import net.jimboi.apricot.stage_c.hoob.world.agents.Town;
import net.jimboi.apricot.stage_c.hoob.world.agents.Traveller;
import net.jimboi.apricot.stage_c.hoob.world.agents.WorldAgent;
import net.jimboi.boron.base.window.input.InputEngine;
import net.jimboi.boron.base.window.input.InputLayer;

import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.view.ScreenSpace;
import org.joml.Vector3f;
import org.zilar.collision.Shape;
import org.zilar.entity.Entity;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Andy on 7/13/17.
 */
public class World implements OldLivingManager.OnLivingCreateListener<WorldAgent>, OldLivingManager.OnLivingDestroyListener<WorldAgent>, InputLayer
{
	public final SceneHoob scene;
	public Vector3f pickPos;

	public Traveller player;
	public OldLivingManager<WorldAgent> agents = new OldLivingManager<>();
	{
		this.agents.onLivingCreate.addListener(this);
		this.agents.onLivingDestroy.addListener(this);
	}

	private ScreenSpace screenSpace;

	public World(SceneHoob scene)
	{
		this.scene = scene;
		OldGameEngine.INPUTENGINE.addInputLayer(this);
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

		this.screenSpace = new ScreenSpace(OldGameEngine.WINDOW.getCurrentViewPort(), this.scene.getRenderer().getCamera(), Direction.CENTER, true, false);
	}

	private boolean actionMove = false;

	public void update(double delta)
	{
		float mouseX = OldInputManager.getInputAmount("mousex");
		float mouseY = OldInputManager.getInputAmount("mousey");
		this.screenSpace.getPoint2DFromScreen(mouseX, mouseY, this.pickPos);

		if (this.actionMove)
		{
			this.player.target.x = this.pickPos.x;
			this.player.target.y = this.pickPos.y;
		}

		this.agents.update();

		this.updateEntities();
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.actionMove = OldInputManager.isInputDown("mouseleft"))
		{
			OldInputManager.consumeInput("mouseleft");
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

	public OldLivingManager<WorldAgent> getAgents()
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
	public void onLivingCreate(WorldAgent agent)
	{
		boolean motion = agent instanceof MoveAgent;
		Shape shape = null;
		if (agent.isSolid())
		{
			if (!motion)
			{
				float x = agent.pos.x();
				float y = agent.pos.y();
				float r = agent.getSize();
				switch (new Random().nextInt(4))
				{
					case 1:
						shape = new Shape.Circle(x, y, r);
						break;
					case 2:
						shape = new Shape.Point(x, y);
						break;
					case 3:
						shape = new Shape.Segment(x, y, r, 0);
						break;
					case 0:
					default:
						shape = new Shape.AABB(x, y, r);
						break;
				}
			}
			else
			{
				shape = new Shape.AABB(agent.pos.x(), agent.pos.y(), agent.getSize());
			}
		}

		Entity entity = this.scene.spawnEntity(agent.pos.x, agent.pos.y, 0, "bunny", "quad", motion, shape, motion);
		entity.addComponent(agent);
	}

	@Override
	public void onLivingDestroy(WorldAgent agent)
	{
		this.scene.getEntityManager().getEntityByComponent(agent).setDead();
	}
}
