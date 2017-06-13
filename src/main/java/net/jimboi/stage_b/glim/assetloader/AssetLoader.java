package net.jimboi.stage_b.glim.assetloader;

import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;
import net.jimboi.stage_b.gnome.resource.ResourceManager;

import org.bstone.json.JSON;
import org.bstone.json.JSONArray;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONString;
import org.bstone.json.JSONValue;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.util.SemanticVersion;
import org.lwjgl.opengl.GL20;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 6/12/17.
 */
public class AssetLoader
{
	private final AssetManager assetManager;
	private final SemanticVersion version;
	private final String domain;

	public AssetLoader(AssetManager assetManager, SemanticVersion version, String domain)
	{
		this.assetManager = assetManager;
		this.version = version;
		this.domain = domain;
	}

	public void loadAssets()
	{
		String domainpath = ResourceManager.getDomainDirectory(this.domain) + "/asset";
		final List<JSONObject> loadlist = new ArrayList<>();
		try
		{
			Files.find(Paths.get(domainpath), 999, (p, attrib) -> (attrib.isRegularFile() && p.getFileName().toString().matches(".*\\.json"))).forEach((p) ->
			{
				FileReader reader = null;
				try
				{
					reader = new FileReader(p.toFile());
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}

				if (reader == null) return;
				try
				{
					JSONValue value = JSON.read(reader);
					System.out.println("Reading file...");
					if (value instanceof JSONObject)
					{
						JSONObject root = (JSONObject) value;
						value = root.get("header");
						if (value instanceof JSONObject)
						{
							JSONObject header = (JSONObject) value;
							System.out.println("Found header: " + header);
							value = header.get("version");
							if (value instanceof JSONString)
							{
								SemanticVersion version = new SemanticVersion(((JSONString) value).get());
								System.out.println("Found version: " + version);
								if (!this.version.isCompatibleWith(version))
								{
									System.out.println("Found asset with incompatible version '" + version + "'!");
									return;
								}

								value = header.get("type");
								switch (((JSONString) value).get())
								{
									case "asset":
										JSONArray assets = (JSONArray) root.get("asset");
										for (int i = 0; i < assets.size(); ++i)
										{
											loadlist.add((JSONObject) assets.get(i));
										}
										break;
									default:
										return;
								}
							}
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Found " + loadlist.size() + " assets.");

		Iterator<JSONObject> iter = loadlist.iterator();

		String id;
		Class type;
		Object[] args;
		ResourceParameter params;
		Asset asset;
		while (iter.hasNext())
		{
			JSONObject obj = iter.next();
			{
				System.out.println("Loading asset: " + obj);
				id = ((JSONString) obj.get("id")).get();
				System.out.println("...with id: '" + id + "'...");
				type = this.getAssetTypeClass(((JSONString) obj.get("type")).get());
				System.out.println("...with type: '" + type + "'...");
				JSONArray requireArray = ((JSONArray) obj.get("require"));
				System.out.println("...with requires: " + requireArray + "...");
				JSONArray paramArray = ((JSONArray) obj.get("param"));
				System.out.println("...with params: " + paramArray + "...");
				if (paramArray != null)
				{
					args = new Object[paramArray.size()];
					for (int i = 0; i < paramArray.size(); ++i)
					{
						args[i] = this.createArgument(paramArray.get(i));
					}
				}
				else
				{
					args = new Object[0];
				}
			}

			System.out.println("Creating parameters...");
			params = this.createParameter(type, args);
			System.out.println("Creating asset...");
			asset = this.createAsset(this.assetManager, type, id, params);
			System.out.println("Registering asset...");
			if (!this.assetManager.registerAsset(type, id, asset, true))
			{
				throw new IllegalStateException("Unable to register asset!");
			}
			System.out.println("Successfully registered asset '" + type.getSimpleName() + "." + id + "'!");
		}
	}

	protected int getClassIntegerConstant(Class constantClass, String fieldName)
	{
		System.out.println("Getting integer constant: " + fieldName);
		try
		{
			Field field = constantClass.getField(fieldName);
			return field.getInt(null);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		throw new UnsupportedOperationException("Unable to find class '" + constantClass.getSimpleName() + "' with field '" + fieldName + "'!");
	}

	protected Object createArgument(JSONValue value)
	{
		System.out.println("Creating argument(" + value + ")...");
		if (value instanceof JSONString)
		{
			String string = ((JSONString) value).get();
			int colon = string.indexOf(':');
			if (colon != -1)
			{
				String head = string.substring(0, colon).trim();
				String body = string.substring(colon + 1).trim();
				switch (head)
				{
					case "res":
						System.out.println("    Creating ResourceLocation at '" + this.domain + ":" + body + "'...");
						return new ResourceLocation(this.domain + ":" + body);
					case "asset":
						System.out.println("    Creating Asset as '" + body + "'...");
						int i = body.indexOf('.');
						if (i != -1)
						{
							Class type = this.getAssetTypeClass(body.substring(0, i));
							String id = body.substring(i + 1);
							Asset asset = this.assetManager.getUnsafeAsset(type, id);
							if (asset == null)
							{
								asset = this.createUnsafeAsset(this.assetManager, type, id);
								if (!this.assetManager.registerAsset(type, id, asset, false))
								{
									throw new IllegalStateException("Found another asset that already exists!");
								}
							}
							return asset;
						}
						else
						{
							throw new AssetFormatException("Must be prefixed with asset type using '.'!");
						}
					case "GL20":
						System.out.println("    Finding OpenGL constant for '" + body + "'...");
						return this.getClassIntegerConstant(GL20.class, body);
					default:
						throw new UnsupportedOperationException("Unable to find variable reference type '" + head + "'!");
				}
			}
			else
			{
				return string;
			}
		}
		else
		{
			throw new UnsupportedOperationException("Unable to create argument for '" + value + "'");
		}
	}

	private void validateArgumentLength(Class type, Object[] args, int length)
	{
		if (args.length >= length) return;

		throw new IllegalArgumentException("Invalid number of arguments for asset type '" + type.getSimpleName() + "'. Must be at least " + length + "!");
	}

	@SuppressWarnings("unchecked")
	private void validateArgument(Class type, Class argType, Object arg)
	{
		if (argType.isInstance(arg)) return;

		throw new IllegalArgumentException("Invalid argument for asset type '" + type.getSimpleName() + "': Expected '" + argType.getSimpleName() + "', but found '" + arg + "'!");
	}

	private void validateArgumentEquals(Class type, Object valid, Object arg)
	{
		if (valid.equals(arg)) return;

		throw new IllegalArgumentException("Invalid argument for asset type '" + type.getSimpleName() + "': Expected '" + valid + "', but found '" + arg + "'!");
	}

	@SuppressWarnings("unchecked")
	protected ResourceParameter createParameter(Class type, Object[] args)
	{
		System.out.println("Creating parameter(" + type.getSimpleName() + ", " + Arrays.asList(args) + ")...");
		if (type.equals(Shader.class))
		{
			this.validateArgumentLength(type, args, 2);
			this.validateArgument(type, ResourceLocation.class, args[0]);
			this.validateArgument(type, Integer.class, args[1]);
			return new ShaderLoader.ShaderParameter((ResourceLocation) args[0], (int) args[1]);
		}
		else if (type.equals(Program.class))
		{
			this.validateArgumentLength(type, args, 1);
			this.validateArgument(type, Asset.class, args[0]);
			this.validateArgumentEquals(type, Shader.class, ((Asset) args[0]).getType());

			ArrayList<Asset<Shader>> shaders = new ArrayList<>();
			shaders.add((Asset<Shader>) args[0]);

			for (int i = 1; i < args.length; ++i)
			{
				Object o = args[i];
				if (o instanceof Asset)
				{
					if (((Asset) o).getType().equals(Shader.class))
					{
						shaders.add((Asset<Shader>) o);
					}
				}
			}
			return new ProgramLoader.ProgramParameter(shaders);
		}
		else
		{
			throw new UnsupportedOperationException("Unable to create parameters for asset type '" + type.getSimpleName() + "'!");
		}
	}

	protected Asset createAsset(AssetManager assetManager, Class type, String id, ResourceParameter params)
	{
		System.out.println("Creating asset(" + type.getSimpleName() + ", " + id + ", " + params + ")...");
		if (type.equals(Shader.class))
		{
			return new Asset<Shader>(assetManager, type, id, params);
		}
		else if (type.equals(Program.class))
		{
			return new Asset<Program>(assetManager, type, id, params);
		}
		else
		{
			throw new UnsupportedOperationException("Unable to create asset for asset type'" + type.getSimpleName() + "'!");
		}
	}

	protected Asset createUnsafeAsset(AssetManager assetManager, Class type, String id)
	{
		return this.createAsset(assetManager, type, id, null);
	}

	protected Class getAssetTypeClass(String type)
	{
		switch (type)
		{
			case "shader":
				return Shader.class;
			case "program":
				return Program.class;
		}

		return Object.class;
	}
}
