package net.jimboi.blob;

import net.jimboi.blob.render.RenderBillboard;
import net.jimboi.blob.render.RenderColor;
import net.jimboi.blob.render.RenderDiffuse;
import net.jimboi.blob.render.RenderLit;
import net.jimboi.blob.render.RenderTexture;
import net.jimboi.mod.Light;
import net.jimboi.mod.meshbuilder.MeshBuilder;
import net.jimboi.mod.meshbuilder.MeshData;
import net.jimboi.mod.meshbuilder.ModelUtil;
import net.jimboi.mod.renderer.Renderer;
import net.jimboi.mod.resource.ResourceLocation;
import net.jimboi.torchlite.MazeWorld;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;
import org.qsilver.render.Render;
import org.qsilver.render.RenderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 4/30/17.
 */
public class RendererBlob extends Renderer implements InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	public static final List<Light> lights = new ArrayList<>();
	public static PerspectiveCamera camera;

	protected final RenderManager renderManager;
	protected final InstanceManager instanceManager;

	private MazeWorld world;

	public RendererBlob()
	{
		RendererBlob.camera = new PerspectiveCamera(640, 480);

		this.world = new MazeWorld(new Random(), 45);
		this.world.generateWorld();

		this.renderManager = new RenderManager();
		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getMaterial().getRenderID());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);
	}

	@Override
	public void onRenderLoad()
	{
		System.out.println("LOADED STUFFF");

		RendererBlob.lights.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 1F, 0));
		RendererBlob.lights.add(Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

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
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Model mod_plane = new Model(ModelUtil.createMesh(mb.bake(false, true)));
		RenderUtil.registerModel("plane", mod_plane);

		//ADD RENDERABLES

		//Create terrain
		this.instanceManager.add(createTerrain(this.world, mat_woodenCrate));

		//Create box
		this.instanceManager.add(new Instance(mod_cube, mat_woodenCrate));
	}

	@Override
	public void onRenderUpdate()
	{
		this.instanceManager.update();
	}

	@Override
	public void onRenderUnload()
	{
		this.instanceManager.destroyAll();
	}

	@Override
	public void onInstanceAdd(InstanceHandler parent, Instance instance)
	{
	}

	@Override
	public void onInstanceRemove(InstanceHandler parent, Instance instance)
	{
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}

	public static Instance createTerrain(MazeWorld world, Material mat)
	{
		int[] map = world.getMap();
		for (int y = 0; y < world.getSize(); ++y)
		{
			for (int x = 0; x < world.getSize(); ++x)
			{
				int i = map[x + y * world.getSize()];
				//Poma.OUT((i == 0 ? " " : i) + " ");
			}
			//Poma.OUT();
		}

		MeshData md = MazeWorld.toMeshBoxData(world, 16, 16);
		Model model = new Model(ModelUtil.createMesh(md));
		RenderUtil.registerModel("terrain", model);

		Instance inst = new Instance(model, mat);
		return inst;
	}
}
