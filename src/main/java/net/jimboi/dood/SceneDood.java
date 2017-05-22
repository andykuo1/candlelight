package net.jimboi.dood;

import net.jimboi.dood.base.EntityInstanceHandler;
import net.jimboi.dood.base.SceneBase;
import net.jimboi.dood.component.ComponentInstanceable;
import net.jimboi.dood.render.RenderBillboard;
import net.jimboi.dood.render.RenderDiffuse;
import net.jimboi.dood.system.ControllerFirstPersonSystem;
import net.jimboi.dood.system.ControllerSideScrollerSystem;
import net.jimboi.dood.system.MotionSystem;
import net.jimboi.mod.Light;
import net.jimboi.mod.RenderUtil;
import net.jimboi.mod.Renderer;
import net.jimboi.mod.meshbuilder.MeshBuilder;
import net.jimboi.mod.meshbuilder.MeshData;
import net.jimboi.mod.meshbuilder.ModelUtil;
import net.jimboi.mod.resource.ResourceLocation;
import net.jimboi.torchlite.World;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.entity.Entity;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.Random;

/**
 * Created by Andy on 5/21/17.
 */
public class SceneDood extends SceneBase
{
	private World world;
	private ControllerFirstPersonSystem controllerFirstPersonSystem;
	private ControllerSideScrollerSystem controllerSideScrollerSystem;
	private MotionSystem motionSystem;
	private Entity entityPlayer;

	public SceneDood()
	{
		Renderer.camera = new PerspectiveCamera(640, 480);
	}

	@Override
	protected void onSceneCreate()
	{
		this.world = new World(new Random(), 45);
		this.world.generateWorld();

		//Add Entities Here
		Renderer.lights.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 1F, 0));
		Renderer.lights.add(Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		this.controllerFirstPersonSystem = new ControllerFirstPersonSystem(this.entityManager, this);
		this.controllerSideScrollerSystem = new ControllerSideScrollerSystem(this.entityManager, this);
		this.motionSystem = new MotionSystem(this.entityManager, this);

		this.entityPlayer = EntityPlayer.create(this.entityManager);
	}

	@Override
	protected void onSceneLoad()
	{
		//Register inputs here
		InputManager.registerMousePosX("mousex");
		InputManager.registerMousePosY("mousey");

		InputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);

		InputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		InputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		InputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		InputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		InputManager.registerKey("up", GLFW.GLFW_KEY_E);
		InputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);

		//Register Renders
		this.renderManager.register("diffuse", new RenderDiffuse(RenderUtil.loadShaderProgram(
				new ResourceLocation("dood:diffuse.vsh"),
				new ResourceLocation("dood:diffuse.fsh"))));
		this.renderManager.register("billboard", new RenderBillboard(RenderUtil.loadShaderProgram(
				new ResourceLocation("dood:billboard.vsh"),
				new ResourceLocation("dood:billboard.fsh")),
				RenderBillboard.Type.CYLINDRICAL));

		//Textures
		Texture tex_bird = RenderUtil.loadTexture(new ResourceLocation("dood:bird.png"));

		//Materials
		Material mat_bird = new Material("diffuse").setTexture(tex_bird);
		RenderUtil.registerMaterial("bird", mat_bird);

		//Models
		Model mod_ball = new Model(RenderUtil.loadMesh(new ResourceLocation("dood:sphere.obj")));
		mod_ball.transformation().translateLocal(0, -1.5F, 0);
		RenderUtil.registerModel("ball", mod_ball);

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector3f(0, 0, 0), new Vector3f(1, 1, 0), new Vector2f(0, 0), new Vector2f(1, 1));
		Model mod_plane = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("plane", mod_plane);

		mb.clear();
		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		Model mod_box = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("box", mod_box);

		this.instanceManager.add(createTerrain(this.world, mat_bird));
	}

	@Override
	protected void onSceneStart()
	{
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		Light light = Renderer.lights.get(0);
		light.position = new Vector4f(Renderer.camera.getTransform().position, 1);
		light.coneDirection = Renderer.camera.getTransform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneDestroy()
	{
	}

	public static Instance createTerrain(World world, Material mat)
	{
		int[] map = world.getMap();
		for (int y = 0; y < world.getSize(); ++y)
		{
			for (int x = 0; x < world.getSize(); ++x)
			{
				int i = map[x + y * world.getSize()];
				System.out.print((i == 0 ? " " : i) + " ");
			}
			System.out.println();
		}

		MeshData md = World.toMeshBoxData(world, 16, 16);
		Model model = new Model(ModelUtil.createMesh(md));
		RenderUtil.registerModel("terrain", model);
		model.transformation().translateLocal(0, 0, -20);

		Instance inst = new Instance(model, mat);
		return inst;
	}

	@Override
	public void onEntityAdd(Entity entity)
	{
		super.onEntityAdd(entity);

		if (entity.hasComponent(ComponentInstanceable.class))
		{
			this.instanceManager.add(new EntityInstanceHandler(entity));
		}
	}
}
