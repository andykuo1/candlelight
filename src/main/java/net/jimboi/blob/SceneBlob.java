package net.jimboi.blob;

import net.jimboi.blob.livings.LivingPlayer;
import net.jimboi.blob.livings.LivingZombie;
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
import org.bstone.poma.Poma;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.Random;

/**
 * Created by Andy on 4/19/17.
 */
public class SceneBlob extends MainScene
{
	private World world;
	private LivingPlayer player;

	public SceneBlob()
	{
		Renderer.camera = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onSceneCreate()
	{
		this.world = new World(new Random(), 45);
		this.world.generateWorld();

		this.player = new LivingPlayer(0, 0, 0);
		this.livingManager.add(this.player);
	}

	@Override
	public void onSceneStart()
	{
		InputManager.registerMousePosX("mousex");
		InputManager.registerMousePosY("mousey");
		InputManager.registerMouseScrollY("scrolly");

		InputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);

		InputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		InputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		InputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		InputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		InputManager.registerKey("up", GLFW.GLFW_KEY_E);
		InputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);

		Renderer.lights.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 1F, 0));
		Renderer.lights.add(Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		this.livingManager.add(new LivingZombie(1, 1, 1));
	}

	@Override
	public void onSceneLoad()
	{
		//LOAD RESOURCES
		this.renderManager.register("color", new RenderColor(RenderUtil.loadShaderProgram(
				new ResourceLocation("blob:colorcube.vsh"),
				new ResourceLocation("blob:colorcube.fsh")
		)));
		this.renderManager.register("texture", new RenderTexture(RenderUtil.loadShaderProgram(
				new ResourceLocation("blob:texcube.vsh"),
				new ResourceLocation("blob:texcube.fsh")
		)));
		this.renderManager.register("diffuse", new RenderDiffuse(RenderUtil.loadShaderProgram(
				new ResourceLocation("blob:diffcube.vsh"),
				new ResourceLocation("blob:diffcube.fsh")
		)));
		this.renderManager.register("lit", new RenderLit(RenderUtil.loadShaderProgram(
				new ResourceLocation("blob:litcube.vsh"),
				new ResourceLocation("blob:litcube.fsh")
		)));
		this.renderManager.register("billboard", new RenderBillboard(RenderUtil.loadShaderProgram(
				new ResourceLocation("blob:billboard.vsh"),
				new ResourceLocation("blob:billboard.fsh")
		), RenderBillboard.Type.CYLINDRICAL));

		//Textures
		Texture tex_woodenCrate = RenderUtil.loadTexture(new ResourceLocation("blob:wooden-crate.jpg"));

		//Materials
		Material mat_woodenCrate = new Material("lit").setTexture(tex_woodenCrate);
		RenderUtil.registerMaterial("woodenCrate", mat_woodenCrate);

		Material mat_billboard = new Material("billboard").setTexture(tex_woodenCrate);
		RenderUtil.registerMaterial("billboard", mat_billboard);

		//Models
		Model mod_woodenCrate = new Model(RenderUtil.loadMesh(new ResourceLocation("blob:cube.obj")));
		RenderUtil.registerModel("woodenCrate", mod_woodenCrate);

		//Custom Models
		MeshBuilder mb = new MeshBuilder();
		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		Model mod_cube = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("cube", mod_cube);

		mb.clear();
		mb.addPlane(new Vector3f(0, 0, 0), new Vector3f(1, 1, 0), new Vector2f(0, 0), new Vector2f(1, 1));
		Model mod_plane = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("plane", mod_plane);

		//ADD RENDERABLES

		//Create terrain
		this.instanceManager.add(createTerrain(this.world, mat_woodenCrate));

		//Create box
		this.instanceManager.add(new Instance(mod_cube, mat_woodenCrate));
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Light light = Renderer.lights.get(0);
		light.position = new Vector4f(Renderer.camera.getTransform().position, 1);
		light.coneDirection = Renderer.camera.getTransform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	@Override
	public void onSceneDestroy()
	{
		Poma.OUT("Destroying Scene . . .");
	}


	public static Instance createTerrain(World world, Material mat)
	{
		int[] map = world.getMap();
		for(int y = 0; y < world.getSize(); ++y)
		{
			for(int x = 0; x < world.getSize(); ++x)
			{
				int i = map[x + y * world.getSize()];
				Poma.OUT((i == 0 ? " " : i) + " ");
			}
			Poma.OUT();
		}

		MeshData md = World.toMeshBoxData(world, 16, 16);
		Model model = new Model(ModelUtil.createMesh(md));
		RenderUtil.registerModel("terrain", model);

		Instance inst = new Instance(model, mat);
		return inst;
	}
}