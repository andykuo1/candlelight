package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.BoundingRenderer;
import net.jimboi.stage_b.glim.renderer.BillboardRenderer;
import net.jimboi.stage_b.glim.renderer.DiffuseRenderer;
import net.jimboi.stage_b.glim.renderer.WireframeRenderer;
import net.jimboi.stage_b.glim.resourceloader.BitmapLoader;
import net.jimboi.stage_b.glim.resourceloader.MeshLoader;
import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.glim.resourceloader.TextureLoader;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.material.MaterialManager;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.util.SemanticVersion;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.renderer.Renderer;
import org.qsilver.util.iterator.FilterIterator;
import org.qsilver.util.map2d.IntMap;
import org.zilar.asset.Asset;
import org.zilar.asset.AssetManager;
import org.zilar.instance.Instance;
import org.zilar.instance.InstanceManager;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TiledTextureAtlas;

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

	protected DiffuseRenderer diffuseRenderer;
	protected BillboardRenderer billboardRenderer;
	protected WireframeRenderer wireframeRenderer;
	protected BoundingRenderer boundingRenderer;

	private BoundingManager boundingManager;

	protected AssetsGlim assets;

	public RendererGlim()
	{
		INSTANCE = this;
		CAMERA = new PerspectiveCamera(640, 480);
	}

	public void setBoundingManager(BoundingManager boundingManager)
	{
		this.boundingManager = boundingManager;
	}

	@Override
	public void onRenderLoad()
	{
		this.instanceManager = new InstanceManager((inst) -> null);
		this.materialManager = new MaterialManager();
		this.assets = new AssetsGlim("glim", new SemanticVersion("0.0.0"));
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

		final AssetManager assets = this.assets.getAssetManager();

		//Bitmaps
		assets.registerLoader(Bitmap.class, new BitmapLoader());
		//Textures
		assets.registerLoader(Texture.class, new TextureLoader());
		//Shaders
		assets.registerLoader(Shader.class, new ShaderLoader());
		//Programs
		assets.registerLoader(Program.class, new ProgramLoader());

		//Meshes
		assets.registerLoader(Mesh.class, new MeshLoader());
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
		this.diffuseRenderer = new DiffuseRenderer(assets.getAsset(Program.class, "diffuse"));
		this.billboardRenderer = new BillboardRenderer(assets.getAsset(Program.class, "billboard"), BillboardRenderer.Type.CYLINDRICAL);
		this.wireframeRenderer = new WireframeRenderer(assets.getAsset(Program.class, "wireframe"));
		this.boundingRenderer = new BoundingRenderer(assets.getAsset(Program.class, "wireframe"));
		this.boundingRenderer.setup(assets);

		//Lights
		RendererGlim.LIGHTS.add(GlimLight.createSpotLight(0, 0, 0, 0xFFFFFF, 0.4F, 0.1F, 0, 15F, 1, 1, 1));
		RendererGlim.LIGHTS.add(GlimLight.createPointLight(0, 0, 0, 0xFFFFFF, 0.6F, 0.1F, 0));
		RendererGlim.LIGHTS.add(GlimLight.createDirectionLight(10000F, 10000F, 10000F, 0xFFFFFF, 0.2F, 0.1F));

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	public void onRenderUpdate()
	{
		this.diffuseRenderer.preRender(CAMERA, this.instanceManager.getInstanceIterator());

		Iterator<Instance> iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getModel().getRenderType().equals("diffuse"));
		this.diffuseRenderer.render(CAMERA, iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getModel().getRenderType().equals("billboard"));
		this.billboardRenderer.render(CAMERA, iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getModel().getRenderType().equals("wireframe"));
		this.wireframeRenderer.render(CAMERA, iter);

		if (this.boundingManager != null)
		{
			this.boundingRenderer.render(CAMERA, this.boundingManager.getBoundingIterator());
		}

		this.instanceManager.update();
		this.assets.update();
	}

	@Override
	public void onRenderUnload()
	{
		this.instanceManager.destroyAll();
		this.assets.unload();

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
		return this.assets.getAssetManager();
	}

	public static MeshData createMeshFromMap(IntMap map, TiledTextureAtlas textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite = textureAtlas.getSprite(0, 0);
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
					if (!isSolid(map, x, y + 1)) tile += 2;
					if (!isSolid(map, x - 1, y)) tile += 4;
					if (!isSolid(map, x, y - 1)) tile += 8;
					sprite = textureAtlas.getSprite(tile, 0);
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
					if (isSolid(map, x, y + 1)) tile += 2;
					if (isSolid(map, x - 1, y)) tile += 4;
					if (isSolid(map, x, y - 1)) tile += 8;
					sprite = textureAtlas.getSprite(tile, 9);
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

		return mb.bake(false, false);
	}

	private static boolean isSolid(IntMap map, int x, int y)
	{
		if (x < 0 || x >= map.width || y < 0 || y >= map.height) return true;
		return map.get(x, y) == 0;
	}
}
