package net.jimboi.apricot.stage_a.blob;

import net.jimboi.apricot.stage_a.mod.ModMaterial;
import net.jimboi.apricot.stage_a.mod.model.Model;
import net.jimboi.apricot.stage_a.mod.resource.ModResourceLocation;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.qsilver.loader.OBJLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Andy on 5/19/17.
 */
public class RenderUtil
{
	private static final Map<String, Model> MODELS = new HashMap<>();
	private static final Map<String, ModMaterial> MATERIALS = new HashMap<>();

	public static Model registerModel(String id, Model model)
	{
		MODELS.put(id, model);
		return model;
	}

	public static ModMaterial registerMaterial(String id, ModMaterial material)
	{
		MATERIALS.put(id, material);
		return material;
	}

	public static Model getModel(String id)
	{
		Model model = MODELS.get(id);
		if (model == null) throw new NoSuchElementException("Could not find model with id " + id);
		return model;
	}

	public static ModMaterial getMaterial(String id)
	{
		ModMaterial material = MATERIALS.get(id);
		if (material == null) throw new NoSuchElementException("Could not find material with id " + id);
		return material;
	}

	public static Mesh loadMesh(ModResourceLocation meshLocation)
	{
		return OBJLoader.read(meshLocation.getFilePath());
	}

	public static Texture loadTexture(ModResourceLocation bitmapLocation)
	{
		return new Texture(new Bitmap(bitmapLocation), GL11.GL_LINEAR, GL12.GL_CLAMP_TO_EDGE);
	}

	public static Program loadShaderProgram(ModResourceLocation vertexShaderLocation, ModResourceLocation fragmentShaderLocation)
	{
		Shader vs = new Shader(vertexShaderLocation, GL20.GL_VERTEX_SHADER);
		Shader fs = new Shader(fragmentShaderLocation, GL20.GL_FRAGMENT_SHADER);
		return new Program().link(vs, fs);
	}
}
