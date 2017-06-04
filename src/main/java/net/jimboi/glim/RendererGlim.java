package net.jimboi.glim;

import net.jimboi.glim.render.RenderGlimBillboard;
import net.jimboi.glim.render.RenderGlimDiffuse;
import net.jimboi.glim.render.RenderGlimWireframe;
import net.jimboi.mod.Light;
import net.jimboi.mod.meshbuilder.MeshBuilder;
import net.jimboi.mod.meshbuilder.ModelUtil;
import net.jimboi.mod.render.Render;
import net.jimboi.mod.render.RenderManager;
import net.jimboi.mod.resource.ResourceLocation;

import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.util.loader.OBJLoader;
import org.bstone.util.map2d.IntMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;
import org.qsilver.renderer.Renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

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
	protected RenderManager renderManager;

	public RendererGlim()
	{
		CAMERA = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onRenderLoad()
	{
		this.instanceManager = new InstanceManager(this::onInstanceRender);
		this.renderManager = new RenderManager();

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

		//Shaders
		register("vertex.diffuse", new Shader(new ResourceLocation("glim:diffuse.vsh"), GL_VERTEX_SHADER));
		register("fragment.diffuse", new Shader(new ResourceLocation("glim:diffuse.fsh"), GL_FRAGMENT_SHADER));
		register("vertex.billboard", new Shader(new ResourceLocation("glim:billboard.vsh"), GL_VERTEX_SHADER));
		register("fragment.billboard", new Shader(new ResourceLocation("glim:billboard.fsh"), GL_FRAGMENT_SHADER));
		register("vertex.wireframe", new Shader(new ResourceLocation("glim:wireframe.vsh"), GL_VERTEX_SHADER));
		register("fragment.wireframe", new Shader(new ResourceLocation("glim:wireframe.fsh"), GL_FRAGMENT_SHADER));

		//Programs
		register("program.diffuse", new Program().link(get("vertex.diffuse"), get("fragment.diffuse")));
		register("program.billboard", new Program().link(get("vertex.billboard"), get("fragment.billboard")));
		register("program.wireframe", new Program().link(get("vertex.wireframe"), get("fragment.wireframe")));

		//Bitmaps
		register("bitmap.bird", new Bitmap(new ResourceLocation("glim:bird.png")));
		register("bitmap.font_basic", new Bitmap(new ResourceLocation("glim:font_basic.png")));
		register("bitmap.wooden_crate", new Bitmap(new ResourceLocation("glim:wooden_crate.jpg")));

		//Textures
		register("texture.bird", new Texture(get("bitmap.bird"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("texture.font", new Texture(get("bitmap.font_basic"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("texture.crate", new Texture(get("bitmap.wooden_crate"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));

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
		register("material.bird", new Material("program.diffuse").setTexture(get("texture.bird")));
		register("material.font", new Material("program.diffuse").setTexture(get("texture.font")));
		register("material.crate", new Material("program.diffuse").setTexture(get("texture.crate")));
		register("material.box", new Material("program.wireframe").setColor(0xFF00FF));

		this.renderManager.register("program.diffuse", new RenderGlimDiffuse(get("program.diffuse")));
		this.renderManager.register("program.billboard", new RenderGlimBillboard(get("program.billboard"), RenderGlimBillboard.Type.CYLINDRICAL));
		this.renderManager.register("program.wireframe", new RenderGlimWireframe(get("program.wireframe")));
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

	private Void onInstanceRender(Instance instance)
	{
		Render render = this.renderManager.get(instance.getMaterial().getRenderID());
		if (render != null) render.onRender(instance);
		return null;
	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}

	public static Mesh createMeshFromMap(IntMap map)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

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
					mb.addBox(wallPos.add(0, -1, 0, u),
							wallPos.add(1, 0, 1, v),
							texTopLeft,
							texBotRight,
							false,
							true,
							false,
							false,
							false,
							false);
				}
				else
				{
					boolean front = !isSolid(map, x, y + 1);
					boolean back = !isSolid(map, x, y - 1);
					boolean left = !isSolid(map, x - 1, y);
					boolean right = !isSolid(map, x + 1, y);

					mb.addBox(wallPos,
							wallPos.add(1, 1, 1, v),
							texTopLeft,
							texBotRight,
							false,
							true,
							front,
							back,
							left,
							right);
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
