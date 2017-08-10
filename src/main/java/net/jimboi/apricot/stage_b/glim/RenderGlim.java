package net.jimboi.apricot.stage_b.glim;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.OldRenderBase;
import net.jimboi.apricot.base.OldSceneBase;
import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.base.render.OldRenderable;
import net.jimboi.apricot.base.renderer.BillboardRenderer;
import net.jimboi.apricot.base.renderer.DiffuseRenderer;
import net.jimboi.apricot.base.renderer.SimpleRenderer;
import net.jimboi.apricot.base.renderer.WireframeRenderer;
import net.jimboi.apricot.base.renderer.shadow.DynamicLight;
import net.jimboi.apricot.stage_b.glim.bounding.BoundingManager;
import net.jimboi.apricot.stage_b.glim.bounding.BoundingRenderer;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.render.RenderEngine;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.resource.MeshLoader;
import org.qsilver.util.iterator.CastIterator;
import org.qsilver.util.iterator.FilterIterator;
import org.zilar.base.Assets;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.resource.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class RenderGlim extends OldRenderBase
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

	public RenderGlim(RenderEngine renderEngine)
	{
		super(renderEngine, new PerspectiveCamera(640, 480));

		this.assets = Assets.create(OldGameEngine.ASSETMANAGER, "glim", new SemanticVersion("0.0.0"));
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
		OldInputManager.registerMousePosX("mousex");
		OldInputManager.registerMousePosY("mousey");

		OldInputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		OldInputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		OldInputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		OldInputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		OldInputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		OldInputManager.registerKey("up", GLFW.GLFW_KEY_E);
		OldInputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		OldInputManager.registerKey("action", GLFW.GLFW_KEY_F);
		OldInputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		final AssetManager assets = OldGameEngine.ASSETMANAGER;

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
		this.simpleRenderer = new SimpleRenderer(renderEngine, assets.getAsset(Program.class, "simple"), this.getCamera(), () -> new FilterIterator<>(scene.getEntityManager().getComponents(EntityComponentRenderable.class, new HashSet<>()).iterator(), (inst) -> inst.getRenderModel().getRenderType().equals("simple")));
		this.simpleRenderer.start();
		this.diffuseRenderer = new DiffuseRenderer(renderEngine, assets, OldGameEngine.WINDOW, this.getCamera(), RenderGlim.LIGHTS, assets.getAsset(Program.class, "diffuse"));
		this.diffuseRenderer.start();
		this.billboardRenderer = new BillboardRenderer(renderEngine, assets.getAsset(Program.class, "billboard"), BillboardRenderer.Type.CYLINDRICAL);
		this.billboardRenderer.start();
		this.wireframeRenderer = new WireframeRenderer(renderEngine, assets.getAsset(Program.class, "wireframe"));
		this.wireframeRenderer.start();
		this.boundingRenderer = new BoundingRenderer(renderEngine, assets.getAsset(Program.class, "wireframe"));
		this.boundingRenderer.start();

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
		OldSceneBase scene = (OldSceneBase) OldGameEngine.SCENEMANAGER.getCurrentScene();
		if (scene == null) return;

		Collection<EntityComponentRenderable> instances = scene.getEntityManager().getComponents(EntityComponentRenderable.class, new HashSet<>());

		Iterator<OldRenderable> iter;

		iter = new CastIterator<>(instances.iterator());
		this.diffuseRenderer.preRender(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getRenderModel().getRenderType().equals("diffuse"));
		this.diffuseRenderer.render(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getRenderModel().getRenderType().equals("billboard"));
		this.billboardRenderer.render(this.getCamera(), iter);

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getRenderModel().getRenderType().equals("wireframe"));
		this.wireframeRenderer.render(this.getCamera(), iter);

		if (this.boundingManager != null)
		{
			this.boundingRenderer.render(this.getCamera(), this.boundingManager.getBoundingIterator());
		}

		iter = new FilterIterator<>(instances.iterator(), (inst) -> inst.getRenderModel().getRenderType().equals("simple"));
		this.simpleRenderer.render(this.getCamera(), iter);
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.assets.unload();

		this.diffuseRenderer.stop();
		this.wireframeRenderer.stop();
		this.billboardRenderer.stop();
		this.simpleRenderer.stop();
		this.boundingRenderer.stop();
	}

	@Override
	public PerspectiveCamera getCamera()
	{
		return (PerspectiveCamera) super.getCamera();
	}
}
