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
import org.bstone.json.JSONBoolean;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONString;
import org.bstone.json.JSONValue;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.util.SemanticVersion;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	enum Mode
	{
		RELEASE,
		DEBUG,
		STRICT
	}

	public static Mode STATE = Mode.DEBUG;

	private final AssetManager assetManager;
	private final SemanticVersion version;
	private final String domain;

	public AssetLoader(AssetManager assetManager, SemanticVersion version, String domain)
	{
		this.assetManager = assetManager;
		this.version = version;
		this.domain = domain;

		this.setup();
	}

	public void setup()
	{
		AssetConstants.clear();
		AssetConstants.registerClassFields(GL11.class);
		AssetConstants.registerClassFields(GL20.class);
		AssetConstants.registerClassFields(GL30.class);

		AssetTypes.clear();
		AssetTypes.registerAssetType(Shader.class, Asset<Shader>::new,
				(type, args) ->
				{
					ResourceParameterProducer.validateArgumentLength(type, args, 2);
					ResourceParameterProducer.validateArgument(type, ResourceLocation.class, args[0]);
					ResourceParameterProducer.validateArgument(type, Integer.class, args[1]);
					return new ShaderLoader.ShaderParameter((ResourceLocation) args[0], (int) args[1]);
				});
		AssetTypes.registerAssetType(Program.class, Asset<Program>::new,
				(type, args) ->
				{
					ResourceParameterProducer.validateArgumentLength(type, args, 1);
					ResourceParameterProducer.validateArgument(type, Asset.class, args[0]);
					ResourceParameterProducer.validateArgumentEquals(type, Shader.class, ((Asset) args[0]).getType());

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
				});

		AssetArguments.clear();
		AssetArguments.registerArgument("res", (body) ->
		{
			System.out.println("    Creating ResourceLocation at '" + this.domain + ":" + body + "'...");
			return new ResourceLocation(this.domain + ":" + body);
		});
		AssetArguments.registerArgument("asset", (body) ->
		{
			System.out.println("    Creating Asset as '" + body + "'...");
			int i = body.indexOf('.');
			if (i != -1)
			{
				Class type = AssetTypes.getAssetType(body.substring(0, i));
				String id = body.substring(i + 1);
				Asset asset = this.assetManager.getUnsafeAsset(type, id);
				if (asset == null)
				{
					if (STATE == Mode.STRICT)
						throw new AssetFormatException("Unable to find asset '" + body + "'!");
					System.err.println("Unable to find asset '" + body + "' (this is a dependency problem!) => Creating a placeholder instead...");
					asset = this.createUnsafeAsset(this.assetManager, type, id);
					if (!this.assetManager.registerAsset(type, id, asset, false))
					{
						throw new IllegalStateException("Found another asset that already exists (although it should not)!");
					}
				}
				return asset;
			}
			else
			{
				throw new AssetFormatException("Must be prefixed with asset type using '.'!");
			}
		});
		AssetArguments.registerArgument("int", (body) ->
		{
			System.out.println("    Finding integer constant for '" + body + "'...");
			int c = body.indexOf('.');
			Class src = AssetConstants.getClass(body.substring(0, c));
			String field = body.substring(c + 1);
			return AssetConstants.getInteger(src, field);
		});
		AssetArguments.registerArgument("float", (body) ->
		{
			System.out.println("    Finding float constant for '" + body + "'...");
			int c = body.indexOf('.');
			Class src = AssetConstants.getClass(body.substring(0, c));
			String field = body.substring(c + 1);
			return AssetConstants.getFloat(src, field);
		});
		AssetArguments.registerArgument("bool", (body) ->
		{
			System.out.println("    Finding boolean constant for '" + body + "'...");
			int c = body.indexOf('.');
			Class src = AssetConstants.getClass(body.substring(0, c));
			String field = body.substring(c + 1);
			return AssetConstants.getBoolean(src, field);
		});
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
								if (!this.version.isCompatibleWith(version))
								{
									System.out.println("Found asset with incompatible version '" + version + "'!");
									return;
								}

								value = header.get("deprecated");
								if (value instanceof JSONBoolean)
								{
									if (((JSONBoolean) value).toBoolean()) return;
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
				type = AssetTypes.getAssetType(((JSONString) obj.get("type")).get());
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
						JSONValue value = paramArray.get(i);
						if (value instanceof JSONString)
						{
							String string = ((JSONString) value).get();
							int colon = string.indexOf(':');
							if (colon != -1)
							{
								String head = string.substring(0, colon).trim();
								String body = string.substring(colon + 1).trim();

								args[i] = this.createArgument(head, body);
							}
							else
							{
								args[i] = string;
							}
						}
						else
						{
							throw new UnsupportedOperationException("Unable to create argument for '" + value + "'");
						}
					}
				}
				else
				{
					args = new Object[0];
				}
			}

			params = this.createParameter(type, args);
			asset = this.createAsset(this.assetManager, type, id, params);
			if (!this.assetManager.registerAsset(type, id, asset, true))
			{
				throw new IllegalStateException("Unable to register asset!");
			}
			System.out.println("Successfully registered asset '" + type.getSimpleName() + "." + id + "'!");
		}
	}

	protected Object createArgument(String argType, String arg)
	{
		if (AssetArguments.isArgumentType(argType))
		{
			System.out.println("     Creating argument: " + arg);
			return AssetArguments.getArgument(argType, arg);
		}
		else
		{
			throw new UnsupportedOperationException("Unable to find argument type '" + argType + "'!");
		}
	}

	protected ResourceParameter createParameter(Class type, Object[] args)
	{
		System.out.println("Creating parameter(" + type.getSimpleName() + ", " + Arrays.asList(args) + ")...");
		ResourceParameter param = AssetTypes.getAssetParameter(type, args);
		if (param == null)
		{
			throw new IllegalArgumentException("Unable to create parameters for asset type '" + type.getSimpleName() + "' with arguments: " + Arrays.asList(args) + "!");
		}
		return param;
	}

	protected Asset createAsset(AssetManager assetManager, Class type, String id, ResourceParameter params)
	{
		System.out.println("Creating asset(" + type.getSimpleName() + ", " + id + ", " + params + ")...");
		Asset asset = AssetTypes.getAsset(assetManager, type, id, params);
		if (asset == null)
		{
			throw new UnsupportedOperationException("Unable to create asset for asset type'" + type.getSimpleName() + "'!");
		}
		return asset;
	}

	protected Asset createUnsafeAsset(AssetManager assetManager, Class type, String id)
	{
		return this.createAsset(assetManager, type, id, null);
	}
}
