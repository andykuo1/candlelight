package net.jimboi.dood;

import net.jimboi.mod2.material.DiffuseMaterial;
import net.jimboi.mod2.meshbuilder.MeshBuilder;
import net.jimboi.mod2.meshbuilder.ModelUtil;
import net.jimboi.mod2.resource.ResourceLocation;

import org.bstone.input.InputManager;
import org.bstone.loader.OBJLoader;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.material.Material;
import org.qsilver.material.MaterialManager;
import org.qsilver.model.Model;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * Created by Andy on 5/28/17.
 */
public class Resources
{
	private static final Map<String, Shader> SHADERS = new HashMap<>();
	private static final Map<String, Program> PROGRAMS = new HashMap<>();
	private static final Map<String, Mesh> MESHES = new HashMap<>();
	private static final Map<String, Bitmap> BITMAPS = new HashMap<>();
	private static final Map<String, Texture> TEXTURES = new HashMap<>();

	private static final Map<String, Model> MODELS = new HashMap<>();
	private static final Map<String, Material> MATERIALS = new HashMap<>();

	public static String register(String id, Shader shader)
	{
		SHADERS.put(id, shader);
		return id;
	}

	public static String register(String id, Program program)
	{
		PROGRAMS.put(id, program);
		return id;
	}

	public static String register(String id, Mesh mesh)
	{
		MESHES.put(id, mesh);
		return id;
	}

	public static String register(String id, Bitmap bitmap)
	{
		BITMAPS.put(id, bitmap);
		return id;
	}

	public static String register(String id, Texture texture)
	{
		TEXTURES.put(id, texture);
		return id;
	}

	public static String register(String id, Model model)
	{
		MODELS.put(id, model);
		return id;
	}

	public static String register(String id, Material material)
	{
		MATERIALS.put(id, material);
		return id;
	}

	public static Shader getShader(String id)
	{
		return SHADERS.get(id);
	}

	public static Program getProgram(String id)
	{
		return PROGRAMS.get(id);
	}

	public static Mesh getMesh(String id)
	{
		return MESHES.get(id);
	}

	public static Bitmap getBitmap(String id)
	{
		return BITMAPS.get(id);
	}

	public static Texture getTexture(String id)
	{
		return TEXTURES.get(id);
	}

	public static Model getModel(String id)
	{
		return MODELS.get(id);
	}

	public static Material getMaterial(String id)
	{
		return MATERIALS.get(id);
	}

	private static MaterialManager materialManager;

	public static void load()
	{
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

		//Shaders
		register("vertex.diffuse", new Shader(new ResourceLocation("dood:diffuse.vsh"), GL_VERTEX_SHADER));
		register("fragment.diffuse", new Shader(new ResourceLocation("dood:diffuse.fsh"), GL_FRAGMENT_SHADER));
		register("vertex.billboard", new Shader(new ResourceLocation("dood:billboard.vsh"), GL_VERTEX_SHADER));
		register("fragment.billboard", new Shader(new ResourceLocation("dood:billboard.fsh"), GL_FRAGMENT_SHADER));

		//Programs
		register("diffuse", new Program().link(getShader("vertex.diffuse"), getShader("fragment.diffuse")));
		register("billboard", new Program().link(getShader("vertex.billboard"), getShader("fragment.billboard")));

		//Bitmaps
		register("bird", new Bitmap(new ResourceLocation("dood:bird.png")));
		register("font_basic", new Bitmap(new ResourceLocation("dood:font_basic.png")));

		//Textures
		register("bird", new Texture(getBitmap("bird"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("font", new Texture(getBitmap("font_basic"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));

		//Meshes
		register("ball", OBJLoader.read(new ResourceLocation("dood:sphere.obj").getFilePath()));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		register("plane", ModelUtil.createMesh(mb.bake(false, true)));
		mb.clear();

		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		register("box", ModelUtil.createMesh(mb.bake(false, true)));

		//Models
		register("ball", new Model(getMesh("ball")));
		register("plane", new Model(getMesh("plane")));
		register("box", new Model(getMesh("box")));

		//Materials
		materialManager = new MaterialManager();
		DiffuseMaterial.setMaterialManager(materialManager);
		register("bird", DiffuseMaterial.create(getTexture("bird")));
		register("font", DiffuseMaterial.create(getTexture("font")));
	}
}
