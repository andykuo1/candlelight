package apricot.stage_a.dood;

import apricot.base.input.OldInputManager;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.resource.ModResourceLocation;

import apricot.bstone.mogli.Bitmap;
import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Program;
import apricot.bstone.mogli.Shader;
import apricot.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import apricot.zilar.loader.OBJLoader;
import apricot.zilar.meshbuilder.MeshBuilder;
import apricot.zilar.meshbuilder.ModelUtil;

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
	private static final Map<String, ModMaterial> MATERIALS = new HashMap<>();

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

	public static String register(String id, ModMaterial material)
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

	public static ModMaterial getMaterial(String id)
	{
		return MATERIALS.get(id);
	}

	public static void load()
	{
		//Register Inputs
		OldInputManager.registerMousePosX("mousex");
		OldInputManager.registerMousePosY("mousey");

		OldInputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);

		OldInputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		OldInputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		OldInputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		OldInputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		OldInputManager.registerKey("up", GLFW.GLFW_KEY_E);
		OldInputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		OldInputManager.registerKey("action", GLFW.GLFW_KEY_F);

		//Shaders
		register("geometry.diffuse", new Shader(new ModResourceLocation("dood:diffuse.vsh"), GL_VERTEX_SHADER));
		register("fragment.diffuse", new Shader(new ModResourceLocation("dood:diffuse.fsh"), GL_FRAGMENT_SHADER));
		register("geometry.billboard", new Shader(new ModResourceLocation("dood:billboard.vsh"), GL_VERTEX_SHADER));
		register("fragment.billboard", new Shader(new ModResourceLocation("dood:billboard.fsh"), GL_FRAGMENT_SHADER));

		//Programs
		register("diffuse", new Program().link(getShader("geometry.diffuse"), getShader("fragment.diffuse")));
		register("billboard", new Program().link(getShader("geometry.billboard"), getShader("fragment.billboard")));

		//Bitmaps
		register("bird", new Bitmap(new ModResourceLocation("dood:bird.png")));
		register("font_basic", new Bitmap(new ModResourceLocation("dood:font_basic.png")));

		//Textures
		register("bird", new Texture(getBitmap("bird"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));
		register("font", new Texture(getBitmap("font_basic"), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE));

		//Meshes
		register("ball", OBJLoader.read(new ModResourceLocation("dood:sphere.obj").getFilePath()));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		register("plane", ModelUtil.createStaticMesh(mb.bake(false, true)));
		mb.clear();

		mb.addBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1), true, true, true, true, true, true);
		register("box", ModelUtil.createStaticMesh(mb.bake(false, true)));

		//Models
		register("ball", new Model(getMesh("ball")));
		register("plane", new Model(getMesh("plane")));
		register("box", new Model(getMesh("box")));

		//Materials
		ModMaterial mat;
		register("bird", mat = new ModMaterial());
		mat.setTexture(getTexture("bird"));
		register("font", mat = new ModMaterial());
		mat.setTexture(getTexture("font"));
	}
}
