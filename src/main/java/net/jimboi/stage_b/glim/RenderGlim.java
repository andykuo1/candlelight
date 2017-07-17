package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.BoundingRenderer;
import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;

import org.bstone.input.InputManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.CameraController;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.Renderable;
import org.qsilver.resource.MeshLoader;
import org.qsilver.util.iterator.CastIterator;
import org.qsilver.util.iterator.FilterIterator;
import org.zilar.base.Assets;
import org.zilar.base.GameEngine;
import org.zilar.base.RenderBase;
import org.zilar.base.SceneBase;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.renderer.BillboardRenderer;
import org.zilar.renderer.DiffuseRenderer;
import org.zilar.renderer.SimpleRenderer;
import org.zilar.renderer.WireframeRenderer;
import org.zilar.renderer.shadow.DynamicLight;
import org.zilar.resource.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class RenderGlim extends RenderBase
{
	public static RenderGlim INSTANCE;
	public static List<DynamicLight> LIGHTS = new ArrayList<>();

	protected SimpleRenderer simpleRenderer;
	protected DiffuseRenderer diffuseRenderer;
	protected BillboardRenderer billboardRenderer;
	protected WireframeRenderer wireframeRenderer;
	protected BoundingRenderer boundingRenderer;

	private BoundingManager boundingManager;

	protected final Assets assets;

	public RenderGlim(CameraController controller)
	{
		super(new PerspectiveCamera(640, 480), controller);

		this.assets = Assets.create("glim", new SemanticVersion("0.0.0"));
		INSTANCE = this;
	}

	public void setBoundingManager(BoundingManager boundingManager)
	{
		this.boundingManager = boundingManager;
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		this.assets.load();

		//Register Inputs
		InputManager.registerMousePosX("mousex");
		InputManager.registerMousePosY("mousey");

		InputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		InputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		InputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		InputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		InputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		InputManager.registerKey("up", GLFW.GLFW_KEY_E);
		InputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		InputManager.registerKey("action", GLFW.GLFW_KEY_F);
		InputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		final AssetManager assets = GameEngine.ASSETMANAGER;

		//Mesh
		Asset<Mesh> msh_ball = assets.registerAsset(Mesh.class, "ball",
				new MeshLoader.MeshParameter(new ResourceLocation("glim:model/sphere.obj")));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Asset<Mesh> msh_plane = assets.registerAsset(Mesh.class, "plane",
				new MeshLoader.MeshParameter(mb.bake(false, true)));
		mb.clear();

		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		Asset<Mesh> msh_box = assets.registerAsset(Mesh.class, "box",
				new MeshLoader.MeshParameter(mb.bake(false, true)));
		mb.clear();

		//Renderers
		this.simpleRenderer = renderEngine.startService(new SimpleRenderer(assets.getAsset(Program.class, "simple")));
		this.diffuseRenderer = renderEngine.startService(new DiffuseRenderer(assets, GameEngine.WINDOW, this.getCamera(), RenderGlim.LIGHTS, assets.getAsset(Program.class, "diffuse")));
		this.billboardRenderer = renderEngine.startService(new BillboardRenderer(assets.getAsset(Program.class, "billboard"), BillboardRenderer.Type.CYLINDRICAL));
		this.wireframeRenderer = renderEngine.startService(new WireframeRenderer(assets.getAsset(Program.class, "wireframe")));
		this.boundingRenderer = renderEngine.startService(new BoundingRenderer(assets.getAsset(Program.class, "wireframe")));

		//Lights
		RenderGlim.LIGHTS.add(DynamicLight.createSpotLight(0, 0, 0, 0xFFFFFF, 0.4F, 0.1F, 0, 15F, 1, 1, 1));
		RenderGlim.LIGHTS.add(DynamicLight.createPointLight(0, 0, 0, 0xFFFFFF, 0.6F, 0.1F, 0));
		RenderGlim.LIGHTS.add(DynamicLight.createDirectionLight(10000F, 10000F, 10000F, 0xFFFFFF, 0.2F, 0.1F));

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	public void onRender(RenderEngine renderEngine)
	{
		SceneBase scene = (SceneBase) GameEngine.SCENEMANAGER.getCurrentScene();
		if (scene == null) return;

		Collection<EntityComponentRenderable> instances = scene.getEntityManager().getComponents(new HashSet<>(), EntityComponentRenderable.class);

		Iterator<Renderable> iter;

		iter = new CastIterator<>(instances.iterator());
		this.diffuseRenderer.preRender(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getModel().getRenderType().equals("diffuse"));
		this.diffuseRenderer.render(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getModel().getRenderType().equals("billboard"));
		this.billboardRenderer.render(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getModel().getRenderType().equals("wireframe"));
		this.wireframeRenderer.render(this.getCamera(), iter);

		if (this.boundingManager != null)
		{
			this.boundingRenderer.render(this.getCamera(), this.boundingManager.getBoundingIterator());
		}

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getModel().getRenderType().equals("simple"));
		this.simpleRenderer.render(this.getCamera(), iter);
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.assets.unload();

		renderEngine.stopService(this.diffuseRenderer);
		renderEngine.stopService(this.wireframeRenderer);
		renderEngine.stopService(this.billboardRenderer);
		renderEngine.stopService(this.simpleRenderer);
		renderEngine.stopService(this.boundingRenderer);
	}

	@Override
	public PerspectiveCamera getCamera()
	{
		return (PerspectiveCamera) super.getCamera();
	}
}
