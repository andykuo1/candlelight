package net.jimboi.stage_b.glim;

import net.jimboi.stage_a.mod.sprite.Sprite;
import net.jimboi.stage_a.mod.sprite.TiledTextureAtlas;
import net.jimboi.stage_b.glim.renderer.BillboardRenderer;
import net.jimboi.stage_b.glim.renderer.DiffuseRenderer;
import net.jimboi.stage_b.glim.renderer.WireframeRenderer;
import net.jimboi.stage_b.glim.resourceloader.BitmapLoader;
import net.jimboi.stage_b.glim.resourceloader.MaterialLoader;
import net.jimboi.stage_b.glim.resourceloader.MeshLoader;
import net.jimboi.stage_b.glim.resourceloader.ModelLoader;
import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.glim.resourceloader.TextureLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.instance.InstanceManager;
import net.jimboi.stage_b.gnome.material.DiffuseMaterial;
import net.jimboi.stage_b.gnome.meshbuilder.MeshBuilder;
import net.jimboi.stage_b.gnome.meshbuilder.ModelUtil;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.material.Material;
import org.bstone.material.MaterialManager;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.model.Model;
import org.qsilver.renderer.Renderer;
import org.qsilver.util.iterator.FilterIterator;
import org.qsilver.util.map2d.IntMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class RendererGlim extends Renderer
{
	public static RendererGlim INSTANCE;
	public static PerspectiveCamera CAMERA;
	public static List<GlimLight> LIGHTS = new ArrayList<>();

	protected InstanceManager instanceManager;
	protected MaterialManager materialManager;
	protected AssetManager assetManager;

	protected DiffuseRenderer diffuseRenderer;
	protected BillboardRenderer billboardRenderer;
	protected WireframeRenderer wireframeRenderer;

	public RendererGlim()
	{
		INSTANCE = this;
		CAMERA = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onRenderLoad()
	{
		this.instanceManager = new InstanceManager((inst) -> null);
		this.materialManager = new MaterialManager();
		this.assetManager = new AssetManager();

		//Register Inputs
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
		InputManager.registerKey("action", GLFW.GLFW_KEY_F);
		InputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		final AssetManager assets = this.assetManager;

		//Bitmaps
		assets.registerLoader(Bitmap.class, new BitmapLoader());
		Asset<Bitmap> bmp_bird = assets.registerAsset(Bitmap.class, "bird",
				new BitmapLoader.BitmapParameter(new ResourceLocation("glim:bird.png")));
		Asset<Bitmap> bmp_font_basic = assets.registerAsset(Bitmap.class, "font_basic",
				new BitmapLoader.BitmapParameter(new ResourceLocation("glim:font_basic.png")));
		Asset<Bitmap> bmp_wooden_crate = assets.registerAsset(Bitmap.class, "wooden_crate",
				new BitmapLoader.BitmapParameter(new ResourceLocation("glim:wooden_crate.jpg")));

		//Textures
		assets.registerLoader(Texture.class, new TextureLoader());
		Asset<Texture> txt_bird = assets.registerAsset(Texture.class, "bird",
				new TextureLoader.TextureParameter(bmp_bird, GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		Asset<Texture> txt_font = assets.registerAsset(Texture.class, "font",
				new TextureLoader.TextureParameter(bmp_font_basic, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE));
		Asset<Texture> txt_crate = assets.registerAsset(Texture.class, "crate",
				new TextureLoader.TextureParameter(bmp_wooden_crate, GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		Asset<Texture> txt_atlas = assets.registerAsset(Texture.class, "atlas",
				new TextureLoader.TextureParameter(bmp_font_basic, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE));

		//Meshes
		assets.registerLoader(Mesh.class, new MeshLoader());
		Asset<Mesh> msh_ball = assets.registerAsset(Mesh.class, "ball",
				new MeshLoader.MeshParameter(new ResourceLocation("glim:sphere.obj")));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Asset<Mesh> msh_plane = assets.registerAsset(Mesh.class, "plane",
				new MeshLoader.MeshParameter(ModelUtil.createMesh(mb.bake(false, true))));
		mb.clear();

		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		Asset<Mesh> msh_box = assets.registerAsset(Mesh.class, "box",
				new MeshLoader.MeshParameter(ModelUtil.createMesh(mb.bake(false, true))));
		mb.clear();

		//Models
		assets.registerLoader(Model.class, new ModelLoader());
		assets.registerAsset(Model.class, "ball",
				new ModelLoader.ModelParameter(msh_ball));
		assets.registerAsset(Model.class, "plane",
				new ModelLoader.ModelParameter(msh_plane));
		assets.registerAsset(Model.class, "box",
				new ModelLoader.ModelParameter(msh_box));

		//Materials
		assets.registerLoader(Material.class, new MaterialLoader(this.materialManager));
		DiffuseMaterial.setMaterialManager(this.materialManager);
		assets.registerAsset(Material.class, "bird",
				new MaterialLoader.MaterialParameter(DiffuseMaterial.getProperties(txt_bird)));
		assets.registerAsset(Material.class, "plane",
				new MaterialLoader.MaterialParameter(DiffuseMaterial.getProperties(txt_bird)));
		assets.registerAsset(Material.class, "font",
				new MaterialLoader.MaterialParameter(DiffuseMaterial.getProperties(txt_font)));
		assets.registerAsset(Material.class, "crate",
				new MaterialLoader.MaterialParameter(DiffuseMaterial.getProperties(txt_crate)));
		assets.registerAsset(Material.class, "box",
				new MaterialLoader.MaterialParameter(DiffuseMaterial.getProperties(0xFF00FF)));

		//Shaders
		assets.registerLoader(Shader.class, new ShaderLoader());

		//Programs
		assets.registerLoader(Program.class, new ProgramLoader());

		this.diffuseRenderer = new DiffuseRenderer(CAMERA);
		this.billboardRenderer = new BillboardRenderer(CAMERA, BillboardRenderer.Type.CYLINDRICAL);
		this.wireframeRenderer = new WireframeRenderer(CAMERA);

		RendererGlim.LIGHTS.add(GlimLight.createSpotLight(0, 0, 0, 0xFFFFFF, 0.4F, 0.1F, 0, 15F, 1, 1, 1));
		RendererGlim.LIGHTS.add(GlimLight.createPointLight(0, 0, 0, 0xFFFFFF, 0.6F, 0.1F, 0));
		RendererGlim.LIGHTS.add(GlimLight.createDirectionLight(10000F, 10000F, 10000F, 0xFFFFFF, 0.2F, 0.1F));

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	public void onRenderUpdate()
	{
		this.diffuseRenderer.preRender(this.instanceManager.getInstanceIterator());

		Iterator<Instance> iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("diffuse"));
		this.diffuseRenderer.render(iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("billboard"));
		this.billboardRenderer.render(iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("wireframe"));
		this.wireframeRenderer.render(iter);

		this.instanceManager.update();
		this.assetManager.update();
	}

	@Override
	public void onRenderUnload()
	{
		this.instanceManager.destroyAll();
		this.assetManager.destroy();

		this.diffuseRenderer.close();
		this.wireframeRenderer.close();
		this.billboardRenderer.close();
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public MaterialManager getMaterialManager()
	{
		return this.materialManager;
	}

	public AssetManager getAssetManager()
	{
		return this.assetManager;
	}

	public static Mesh createMeshFromMap(IntMap map, TiledTextureAtlas textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite = textureAtlas.getSprite(5, 8);
		Vector2f texSideTopLeft = new Vector2f(sprite.getU(), sprite.getV());
		Vector2f texSideBotRight = new Vector2f(texSideTopLeft).add(sprite.getWidth(), sprite.getHeight());

		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f();
		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
			{
				Vector3fc wallPos = new Vector3f(x, 0, y);
				u.set(0);
				v.set(0);

				if (!isSolid(map, x, y))
				{
					int tile = 0;
					if (!isSolid(map, x + 1, y)) tile += 1;
					if (!isSolid(map, x, y - 1)) tile += 2;
					if (!isSolid(map, x - 1, y)) tile += 4;
					if (!isSolid(map, x, y + 1)) tile += 8;
					sprite = textureAtlas.getSprite(tile, 9);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					mb.addBox(wallPos.add(0, -1, 0, u),
							wallPos.add(1, 0, 1, v),
							texTopLeft, texBotRight,
							false,
							true,
							false,
							false,
							false,
							false);
				}
				else
				{
					int tile = 0;
					if (isSolid(map, x + 1, y)) tile += 1;
					if (isSolid(map, x, y - 1)) tile += 2;
					if (isSolid(map, x - 1, y)) tile += 4;
					if (isSolid(map, x, y + 1)) tile += 8;
					sprite = textureAtlas.getSprite(tile, 0);
					texTopLeft.set(sprite.getU(), sprite.getV());
					texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

					boolean front = !isSolid(map, x, y + 1);
					boolean back = !isSolid(map, x, y - 1);
					boolean left = !isSolid(map, x - 1, y);
					boolean right = !isSolid(map, x + 1, y);

					mb.addTexturedBox(wallPos,
							wallPos.add(1, 2, 1, v),
							texSideTopLeft, texSideBotRight,//Front
							texSideTopLeft, texSideBotRight,//Back
							texTopLeft, texBotRight,//Top
							texTopLeft, texBotRight,//Bot
							texSideTopLeft, texSideBotRight,//Left
							texSideTopLeft, texSideBotRight,//Right
							false,
							true,
							front,
							back,
							left,
							right,
							true);
				}
			}
		}

		return ModelUtil.createMesh(mb.bake(false, false));
	}

	private static boolean isSolid(IntMap map, int x, int y)
	{
		if (x < 0 || x >= map.width || y < 0 || y >= map.height) return true;
		return map.get(x, y) == 0;
	}
}
