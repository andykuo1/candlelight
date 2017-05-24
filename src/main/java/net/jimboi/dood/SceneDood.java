package net.jimboi.dood;

import net.jimboi.base.Main;
import net.jimboi.dood.base.SceneBase;
import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.dood.render.RenderBillboard;
import net.jimboi.dood.render.RenderDiffuse;
import net.jimboi.dood.system.EntitySystem;
import net.jimboi.dood.system.SystemAnimatedTexture;
import net.jimboi.dood.system.SystemBox2D;
import net.jimboi.dood.system.SystemControllerFirstPerson;
import net.jimboi.dood.system.SystemControllerSideScroll;
import net.jimboi.dood.system.SystemControllerSideScrollBox2D;
import net.jimboi.dood.system.SystemInstance;
import net.jimboi.dood.system.SystemMotion;
import net.jimboi.mod.Light;
import net.jimboi.mod.RenderUtil;
import net.jimboi.mod.Renderer;
import net.jimboi.mod.meshbuilder.MeshBuilder;
import net.jimboi.mod.meshbuilder.ModelUtil;
import net.jimboi.mod.resource.ResourceLocation;
import net.jimboi.mod.transform.Transform3;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.entity.Entity;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

/**
 * Created by Andy on 5/21/17.
 */
public class SceneDood extends SceneBase implements InputEngine.OnInputUpdateListener
{
	private WorldGenTerrain terrain;
	private WorldGenHills hills;

	private SystemInstance systemInstance;
	private SystemControllerFirstPerson systemControllerFirstPerson;
	private SystemControllerSideScroll systemControllerSideScroll;
	private SystemControllerSideScrollBox2D systemControllerSideScrollBox2D;
	private SystemMotion systemMotion;
	private SystemBox2D systemBox2D;
	private SystemAnimatedTexture systemAnimatedTexture;

	private Entity entityPlayer;

	public SceneDood()
	{
		Renderer.camera = new PerspectiveCamera(640, 480);
		Main.INPUTENGINE.onInputUpdate.addListener(this);
	}

	@Override
	protected void onSceneCreate()
	{
		//Add Entities Here
		Renderer.lights.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 1F, 0));
		Renderer.lights.add(Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		this.systemInstance = new SystemInstance(this.entityManager, this.instanceManager);
		this.systemControllerFirstPerson = new SystemControllerFirstPerson(this.entityManager, this);
		this.systemControllerSideScroll = new SystemControllerSideScroll(this.entityManager, this);
		this.systemMotion = new SystemMotion(this.entityManager, this);
		this.systemBox2D = new SystemBox2D(this.entityManager, this);
		this.systemControllerSideScrollBox2D = new SystemControllerSideScrollBox2D(this.entityManager, this);
		this.systemAnimatedTexture = new SystemAnimatedTexture(this.entityManager, this);

		this.systemInstance.start();
		//this.systemControllerFirstPerson.start();
		//this.systemControllerSideScroll.start();
		this.systemMotion.start();
		this.systemBox2D.start();
		//this.systemControllerSideScrollBox2D.start();
		this.systemAnimatedTexture.start();
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
		Texture tex_font = RenderUtil.loadTexture(new ResourceLocation("dood:font_basic.png"));

		//Materials
		Material mat_bird = new Material("diffuse").setTexture(tex_bird);
		RenderUtil.registerMaterial("bird", mat_bird);

		Material mat_font = new Material("diffuse").setTexture(tex_font);
		RenderUtil.registerMaterial("font", mat_font);

		//Models
		Model mod_ball = new Model(RenderUtil.loadMesh(new ResourceLocation("dood:sphere.obj")));
		RenderUtil.registerModel("ball", mod_ball);

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Model mod_plane = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("plane", mod_plane);

		mb.clear();
		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		Model mod_box = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("box", mod_box);

		this.hills = new WorldGenHills();
		hills.generate();
		Model mod_hills = new Model(hills.createMeshFromVertices());
		RenderUtil.registerModel("hills", mod_hills);

		this.terrain = new WorldGenTerrain();
		terrain.generate();
		Model mod_dungeon = new Model(terrain.createMeshFromVertices());
		RenderUtil.registerModel("dungeon", mod_dungeon);

		//this.instanceManager.add(new Instance(mod_hills, mat_bird));
		//this.instanceManager.add(new Instance(mod_dungeon, mat_bird));
	}

	@Override
	protected void onSceneStart()
	{
		this.entityPlayer = EntityPlayer.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBox.create(this.entityManager);
		EntityBall.create(this.entityManager);
		EntityHills.create(this.entityManager, this.hills);

		Renderer.camera.setCameraController(new CameraControllerBox2D((Transform3) this.entityPlayer.getComponent(ComponentTransform.class).transform, this.entityPlayer.getComponent(ComponentBox2DBody.class).handler));
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
	public void onInputUpdate(InputEngine inputEngine)
	{
		Renderer.camera.update(inputEngine);
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
