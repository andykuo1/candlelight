package net.jimboi.glim;

import net.jimboi.glim.renderer.BillboardRenderer;
import net.jimboi.glim.renderer.DiffuseRenderer;
import net.jimboi.glim.renderer.WireframeRenderer;
import net.jimboi.mod.Light;
import net.jimboi.mod2.instance.Instance;
import net.jimboi.mod2.instance.InstanceManager;
import net.jimboi.mod2.meshbuilder.MeshBuilder;
import net.jimboi.mod2.meshbuilder.ModelUtil;
import net.jimboi.mod2.resource.ResourceLocation;
import net.jimboi.mod2.sprite.Sprite;
import net.jimboi.mod2.sprite.TiledTextureAtlas;

import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.util.iterator.FilterIterator;
import org.bstone.util.loader.OBJLoader;
import org.bstone.util.map2d.IntMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.render.Material;
import org.qsilver.render.Model;
import org.qsilver.renderer.Renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 6/1/17.
 */
public class RendererGlim extends Renderer
{
	private static final Map<String, Object> RESOURCES = new HashMap<>();

	public static <T> T register(String id, T value)
	{
		RESOURCES.put(id, value);
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String id)
	{
		return (T) RESOURCES.get(id);
	}

	public static Camera CAMERA;
	public static List<Light> LIGHTS = new ArrayList<>();

	protected InstanceManager instanceManager;

	protected DiffuseRenderer diffuseRenderer;
	protected BillboardRenderer billboardRenderer;
	protected WireframeRenderer wireframeRenderer;

	public RendererGlim()
	{
		CAMERA = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onRenderLoad()
	{
		this.instanceManager = new InstanceManager((inst) -> null);

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

		//Bitmaps
		register("bitmap.bird", new Bitmap(new ResourceLocation("glim:bird.png")));
		register("bitmap.font_basic", new Bitmap(new ResourceLocation("glim:font_basic.png")));
		register("bitmap.wooden_crate", new Bitmap(new ResourceLocation("glim:wooden_crate.jpg")));

		//Textures
		register("texture.bird", new Texture(get("bitmap.bird"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("texture.font", new Texture(get("bitmap.font_basic"), GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE));
		register("texture.crate", new Texture(get("bitmap.wooden_crate"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("texture.atlas", new Texture(get("bitmap.font_basic"), GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE));

		//Meshes
		register("mesh.ball", OBJLoader.read(new ResourceLocation("glim:sphere.obj").getFilePath()));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		register("mesh.plane", ModelUtil.createMesh(mb.bake(false, true)));
		mb.clear();

		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		register("mesh.box", ModelUtil.createMesh(mb.bake(false, true)));

		//Models
		register("model.ball", new Model(get("mesh.ball")));
		register("model.plane", new Model(get("mesh.plane")));
		register("model.box", new Model(get("mesh.box")));

		//Materials
		register("material.bird", new Material().setTexture(get("texture.bird")));
		register("material.plane", new Material().setTexture(get("texture.bird")));
		register("material.font", new Material().setTexture(get("texture.font")));
		register("material.crate", new Material().setTexture(get("texture.crate")));
		register("material.box", new Material().setColor(0xFF00FF));

		this.diffuseRenderer = new DiffuseRenderer(CAMERA);
		this.billboardRenderer = new BillboardRenderer(CAMERA, BillboardRenderer.Type.CYLINDRICAL);
		this.wireframeRenderer = new WireframeRenderer(CAMERA);

		RendererGlim.LIGHTS.add(Light.createSpotLight(0, 0, 0, 0xFFFFFF, 0.4F, 0.1F, 0, 15F, 1, 1, 1));
		RendererGlim.LIGHTS.add(Light.createPointLight(0, 0, 0, 0xFFFFFF, 0.6F, 0.1F, 0));
		RendererGlim.LIGHTS.add(Light.createDirectionLight(1000, 1000F, 1000F, 0xFFFFFF, 0.1F, 0.06F));
	}

	@Override
	public void onRenderUpdate()
	{
		Iterator<Instance> iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("diffuse"));
		this.diffuseRenderer.render(iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("billboard"));
		this.billboardRenderer.render(iter);

		iter = new FilterIterator<>(this.instanceManager.getInstanceIterator(), (inst) -> inst.getRenderType().equals("wireframe"));
		this.wireframeRenderer.render(iter);

		this.instanceManager.update();
	}

	@Override
	public void onRenderUnload()
	{
		this.instanceManager.destroyAll();
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
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
							wallPos.add(1, 1, 1, v),
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
