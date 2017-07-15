package net.jimboi.stage_a.blob;

import net.jimboi.stage_a.blob.render.RenderBillboard;
import net.jimboi.stage_a.blob.render.RenderColor;
import net.jimboi.stage_a.blob.render.RenderDiffuse;
import net.jimboi.stage_a.blob.render.RenderLit;
import net.jimboi.stage_a.blob.render.RenderTexture;
import net.jimboi.stage_a.blob.torchlite.MazeWorld;
import net.jimboi.stage_a.mod.ModLight;
import net.jimboi.stage_a.mod.ModMaterial;
import net.jimboi.stage_a.mod.instance.Instance;
import net.jimboi.stage_a.mod.instance.InstanceHandler;
import net.jimboi.stage_a.mod.instance.InstanceManager;
import net.jimboi.stage_a.mod.model.Model;
import net.jimboi.stage_a.mod.render.Render;
import net.jimboi.stage_a.mod.renderer.OldRenderService;
import net.jimboi.stage_a.mod.resource.ModResourceLocation;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.qsilver.renderer.RenderEngine;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 4/30/17.
 */
public class RendererBlob extends OldRenderService implements InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	public static final List<ModLight> lights = new ArrayList<>();
	public static PerspectiveCamera camera;

	protected final net.jimboi.stage_a.mod.render.RenderManager renderManager;
	protected final InstanceManager instanceManager;

	private MazeWorld world;

	public RendererBlob()
	{
		RendererBlob.camera = new PerspectiveCamera(640, 480);

		this.world = new MazeWorld(new Random(), 45);
		this.world.generateWorld();

		this.renderManager = new net.jimboi.stage_a.mod.render.RenderManager();
		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getRenderType());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		RendererBlob.lights.add(ModLight.createPointLight(0, 0, 0, 0xFFFFFF, 1F, 1F, 0));
		RendererBlob.lights.add(ModLight.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		//LOAD RESOURCES
		this.renderManager.register("color", new RenderColor(RenderUtil.loadShaderProgram(
				new ModResourceLocation("blob:colorcube.vsh"),
				new ModResourceLocation("blob:colorcube.fsh")
		)));
		this.renderManager.register("texture", new RenderTexture(RenderUtil.loadShaderProgram(
				new ModResourceLocation("blob:texcube.vsh"),
				new ModResourceLocation("blob:texcube.fsh")
		)));
		this.renderManager.register("diffuse", new RenderDiffuse(RenderUtil.loadShaderProgram(
				new ModResourceLocation("blob:diffcube.vsh"),
				new ModResourceLocation("blob:diffcube.fsh")
		)));
		this.renderManager.register("lit", new RenderLit(RenderUtil.loadShaderProgram(
				new ModResourceLocation("blob:litcube.vsh"),
				new ModResourceLocation("blob:litcube.fsh")
		)));
		this.renderManager.register("billboard", new RenderBillboard(RenderUtil.loadShaderProgram(
				new ModResourceLocation("blob:billboard.vsh"),
				new ModResourceLocation("blob:billboard.fsh")
		), RenderBillboard.Type.CYLINDRICAL));

		//Textures
		Texture tex_woodenCrate = RenderUtil.loadTexture(new ModResourceLocation("blob:wooden-crate.jpg"));

		//Materials
		ModMaterial mat_woodenCrate = new ModMaterial();
		mat_woodenCrate.setTexture(tex_woodenCrate);
		RenderUtil.registerMaterial("woodenCrate", mat_woodenCrate);

		ModMaterial mat_billboard = new ModMaterial();
		mat_billboard.setTexture(tex_woodenCrate);
		RenderUtil.registerMaterial("billboard", mat_billboard);

		//Models
		Model mod_woodenCrate = new Model(RenderUtil.loadMesh(new ModResourceLocation("blob:cube.obj")));
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
		this.instanceManager.add(new Instance(mod_cube, mat_woodenCrate, "lit"));
	}

	@Override
	public void onRender(RenderEngine renderEngine)
	{
		this.instanceManager.update();
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
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

	public net.jimboi.stage_a.mod.render.RenderManager getRenderManager()
	{
		return this.renderManager;
	}

	public static Instance createTerrain(MazeWorld world, ModMaterial mat)
	{
		int[] map = world.getMap();
		MeshData md = MazeWorld.toMeshBoxData(world, 16, 16);
		Model model = new Model(ModelUtil.createMesh(md));
		RenderUtil.registerModel("terrain", model);

		Instance inst = new Instance(model, mat, "lit");
		return inst;
	}
}
