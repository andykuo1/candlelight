package org.qsilver.asset.assetloader;

import org.bstone.json.JSON;
import org.bstone.json.JSONArray;
import org.bstone.json.JSONBoolean;
import org.bstone.json.JSONLiteral;
import org.bstone.json.JSONNumber;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONString;
import org.bstone.json.JSONValue;
import org.bstone.util.SemanticVersion;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.asset.ResourceParameter;
import org.zilar.resource.ResourceLocation;

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
	public enum Mode
	{
		RELEASE,
		DEBUG,
		STRICT
	}

	public static final Mode STATE = Mode.DEBUG;
	public static final int SEARCH_DEPTH = 999;

	private final AssetManager assetManager;
	private final SemanticVersion version;
	private final ResourceLocation assetDirectory;

	public AssetLoader(AssetManager assetManager, SemanticVersion version, ResourceLocation assetDirectory)
	{
		this.assetManager = assetManager;
		this.version = version;
		this.assetDirectory = assetDirectory;
	}

	public void loadAssets()
	{
		final List<JSONObject> loadlist = new ArrayList<>();
		try
		{
			Files.find(Paths.get(this.assetDirectory.getFilePath()), SEARCH_DEPTH, (p, attrib) -> (attrib.isRegularFile() && p.getFileName().toString().matches(".*\\.json"))).forEach((p) ->
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
											JSONObject obj = (JSONObject) assets.get(i);

											value = obj.get("deprecated");
											if (value instanceof JSONBoolean)
											{
												if (((JSONBoolean) value).toBoolean()) continue;
											}
											else
											{
												loadlist.add((JSONObject) assets.get(i));
											}
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
				System.out.println("Loading asset:");
				System.out.println(" + " + obj);
				id = ((JSONString) obj.get("id")).get();
				type = AssetTypes.getAssetType(((JSONString) obj.get("type")).get());
				//TODO: Implement requirements
				JSONArray requireArray = ((JSONArray) obj.get("require"));
				JSONArray paramArray = ((JSONArray) obj.get("param"));
				if (paramArray != null)
				{
					args = createArguments(paramArray);
				}
				else
				{
					args = new Object[0];
				}
			}

			params = createParameter(type, args);
			asset = createAsset(this.assetManager, type, id, params);
			if (!this.assetManager.registerAsset(type, id, asset, true))
			{
				throw new IllegalStateException("Unable to register asset!");
			}
			System.out.println("Successfully registered asset '" + type.getSimpleName() + "." + id + "'!");
		}
	}

	public static Object[] createArguments(JSONArray argList)
	{
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); ++i)
		{
			JSONValue value = argList.get(i);
			if (value instanceof JSONString)
			{
				String string = ((JSONString) value).get();
				int colon = string.indexOf(':');
				if (colon > 0 && string.charAt(colon - 1) == '\\')
				{
					colon = -1;
				}

				if (colon != -1)
				{
					String head = string.substring(0, colon).trim();
					String body = string.substring(colon + 1).trim();

					args[i] = createArgument(head, body);
				}
				else
				{
					System.out.println(" - Creating string...");
					args[i] = string;
				}
			}
			else if (value instanceof JSONObject)
			{
				System.out.println(" - Creating object...");
				JSONObject object = (JSONObject) value;
				JSONString className = (JSONString) object.get("class");
				JSONArray classArgs = (JSONArray) object.get("args");

				args[i] = createObject(className.get(), createArguments(classArgs));
			}
			else if (value instanceof JSONNumber)
			{
				JSONNumber number = (JSONNumber) value;
				if (number.isDecimal())
				{
					System.out.println(" - Creating float...");
					args[i] = number.toFloat();
				}
				else
				{
					System.out.println(" - Creating int...");
					args[i] = number.toInt();
				}
			}
			else if (value instanceof JSONBoolean)
			{
				System.out.println(" - Creating boolean...");
				JSONBoolean bool = (JSONBoolean) value;
				args[i] = bool.toBoolean();
			}
			else if (value instanceof JSONLiteral)
			{
				System.out.println(" - Creating literal...");
				JSONLiteral literal = (JSONLiteral) value;
				if (literal.isNull())
				{
					args[i] = null;
				}
				else
				{
					throw new IllegalArgumentException("Unknown literal value '" + literal.toLiteral() + "'!");
				}
			}
			else
			{
				throw new UnsupportedOperationException("Unable to create argument for '" + value + "'");
			}
		}

		return args;
	}

	public static Object createArgument(String argType, String arg)
	{
		if (AssetArguments.isArgumentType(argType))
		{
			return AssetArguments.getArgument(argType, arg);
		}
		else
		{
			throw new UnsupportedOperationException("Unable to find argument type '" + argType + "'!");
		}
	}

	public static ResourceParameter createParameter(Class type, Object[] args)
	{
		System.out.println(" > Creating parameters...");
		ResourceParameter param = AssetTypes.getAssetParameter(type, args);
		if (param == null)
		{
			throw new IllegalArgumentException("Unable to create parameters for asset type '" + type.getSimpleName() + "' with arguments: " + Arrays.asList(args) + "!");
		}
		return param;
	}

	public static Asset createAsset(AssetManager assetManager, Class type, String id, ResourceParameter params)
	{
		System.out.println(" > Creating asset...");
		Asset asset = AssetTypes.getAsset(assetManager, type, id, params);
		if (asset == null)
		{
			throw new UnsupportedOperationException("Unable to create asset for asset type'" + type.getSimpleName() + "'!");
		}
		return asset;
	}

	public static Asset createUnsafeAsset(AssetManager assetManager, Class type, String id)
	{
		return createAsset(assetManager, type, id, null);
	}

	@SuppressWarnings("unchecked")
	public static Object createObject(String classpath, Object[] args)
	{
		//TODO: null's are a pain to get the type for. . .
		try
		{
			Class objClass = Class.forName(classpath);
			Class[] argTypes = new Class[args.length];
			for(int i = 0; i < argTypes.length; ++i)
			{
				Class type = args[i].getClass();
				if (type.equals(Integer.class)) type = int.class;
				if (type.equals(Float.class)) type = float.class;
				if (type.equals(Boolean.class)) type = boolean.class;
				argTypes[i] = type;
			}

			try
			{
				return objClass.getConstructor(argTypes).newInstance(args);
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
				throw new IllegalArgumentException("Unable to find constructor for class '" + classpath + "' with argument types: " + Arrays.asList(argTypes) + "!");
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Unable to create object of class '" + classpath + "' with arguments: " + Arrays.asList(args) + "!");
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException("Unable to find class with full name: '" + classpath + "'!");
		}
	}
}
