package net.jimboi.apricot.base.assets;

import net.jimboi.apricot.base.assets.resource.BitmapLoader;
import net.jimboi.apricot.base.assets.resource.MeshLoader;
import net.jimboi.apricot.base.assets.resource.ProgramLoader;
import net.jimboi.apricot.base.assets.resource.ShaderLoader;
import net.jimboi.apricot.base.assets.resource.TextureAtlasLoader;
import net.jimboi.apricot.base.assets.resource.TextureLoader;
import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.asset.AssetLoadable;
import net.jimboi.boron.base_ab.asset.AssetManager;
import net.jimboi.boron.base_ab.asset.assetloader.AssetArguments;
import net.jimboi.boron.base_ab.asset.assetloader.AssetConstants;
import net.jimboi.boron.base_ab.asset.assetloader.AssetFormatException;
import net.jimboi.boron.base_ab.asset.assetloader.AssetLoader;
import net.jimboi.boron.base_ab.asset.assetloader.AssetTypes;
import net.jimboi.boron.base_ab.asset.assetloader.ResourceParameterProducer;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.util.SemanticVersion;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.qsilver.ResourceLocation;

import java.util.ArrayList;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class Assets
{
	private Assets() {}

	public static Assets create(AssetManager assetManager, String domain, SemanticVersion version)
	{
		if (version.isCompatibleWith(Asset00.VERSION))
		{
			return new Asset00(assetManager, domain);
		}

		throw new IllegalArgumentException("Unable to find asset loader for version '" + version + "'");
	}

	private final static class Asset00 extends Assets
	{
		private static final SemanticVersion VERSION = new SemanticVersion("0.0.0");

		private final String domain;
		private AssetManager assetManager;
		private AssetLoader assetLoader;

		public Asset00(AssetManager assetManager, String domain)
		{
			this.domain = domain;
			this.assetManager = assetManager;
			this.assetLoader = new AssetLoader(this.assetManager, VERSION, new ResourceLocation(this.domain + ":"));
		}

		@SuppressWarnings("RedundantTypeArguments")
		private void setupAssetLoader()
		{
			AssetConstants.clear();
			AssetConstants.registerClassFields(GL11.class);
			AssetConstants.registerClassFields(GL12.class);
			AssetConstants.registerClassFields(GL20.class);
			AssetConstants.registerClassFields(GL30.class);

			AssetTypes.clear();
			AssetTypes.registerAssetType(Shader.class, AssetLoadable<Shader>::new,
					(type, args) ->
					{
						ResourceParameterProducer.validateArgumentLength(type, args, 2);
						ResourceParameterProducer.validateArgument(type, ResourceLocation.class, args[0]);
						ResourceParameterProducer.validateArgument(type, Integer.class, args[1]);
						return new ShaderLoader.ShaderParameter((ResourceLocation) args[0], (int) args[1]);
					});
			AssetTypes.registerAssetType(Program.class, AssetLoadable<Program>::new,
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
			AssetTypes.registerAssetType(Bitmap.class, AssetLoadable<Bitmap>::new,
					(type, args) ->
					{
						ResourceParameterProducer.validateArgumentLength(type, args, 1);
						ResourceParameterProducer.validateArgument(type, ResourceLocation.class, args[0]);
						return new BitmapLoader.BitmapParameter((ResourceLocation) args[0]);
					});
			AssetTypes.registerAssetType(Texture.class, AssetLoadable<Texture>::new,
					(type, args) ->
					{
						ResourceParameterProducer.validateArgumentLength(type, args, 3);
						if (args[0] instanceof Asset)
						{
							ResourceParameterProducer.validateArgumentEquals(type, Bitmap.class, ((Asset) args[0]).getType());
							ResourceParameterProducer.validateArgument(type, Integer.class, args[1]);
							ResourceParameterProducer.validateArgument(type, Integer.class, args[2]);
							return new TextureLoader.TextureParameter((Asset<Bitmap>) args[0], (int) args[1], (int) args[2]);
						}
						else
						{
							ResourceParameterProducer.validateArgumentLength(type, args, 5);
							ResourceParameterProducer.validateArgument(type, Integer.class, args[0]);
							ResourceParameterProducer.validateArgument(type, Integer.class, args[1]);
							ResourceParameterProducer.validateArgument(type, Integer.class, args[2]);
							ResourceParameterProducer.validateArgument(type, Integer.class, args[3]);
							ResourceParameterProducer.validateArgument(type, String.class, args[4]);
							return new TextureLoader.TextureParameter((int) args[0], (int) args[1], (int) args[2], (int) args[3], Bitmap.Format.valueOf((String) args[4]));
						}
					});

			AssetArguments.clear();
			AssetArguments.registerArgument("res", (body) ->
			{
				System.out.println(" - Creating ResourceLocation at '" + this.domain + ":" + body + "'...");
				return new ResourceLocation(this.domain + ":" + body);
			});
			AssetArguments.registerArgument("asset", (body) ->
			{
				System.out.println(" - Creating Asset as '" + body + "'...");
				int i = body.indexOf('.');
				if (i != -1)
				{
					Class type = AssetTypes.getAssetType(body.substring(0, i));
					String id = body.substring(i + 1);
					Asset asset = this.assetManager.getUnsafeAsset(type, id);
					if (asset == null)
					{
						if (AssetLoader.STATE == AssetLoader.Mode.STRICT)
							throw new AssetFormatException("Unable to find asset '" + body + "'!");
						System.err.println("Unable to find asset '" + body + "' (this is a dependency problem!) => Creating a placeholder instead...");
						asset = AssetLoader.createUnsafeAsset(this.assetManager, type, id);
						if (!this.assetManager.registerAsset(type, id, asset))
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
				System.out.println(" - Finding integer constant for '" + body + "'...");
				int c = body.indexOf('.');
				Class src = AssetConstants.getClass(body.substring(0, c));
				String field = body.substring(c + 1);
				return AssetConstants.getInteger(src, field);
			});
			AssetArguments.registerArgument("float", (body) ->
			{
				System.out.println(" - Finding float constant for '" + body + "'...");
				int c = body.indexOf('.');
				Class src = AssetConstants.getClass(body.substring(0, c));
				String field = body.substring(c + 1);
				return AssetConstants.getFloat(src, field);
			});
			AssetArguments.registerArgument("bool", (body) ->
			{
				System.out.println(" - Finding boolean constant for '" + body + "'...");
				int c = body.indexOf('.');
				Class src = AssetConstants.getClass(body.substring(0, c));
				String field = body.substring(c + 1);
				return AssetConstants.getBoolean(src, field);
			});
		}

		@Override
		public void load()
		{
			this.setupAssetLoader();
			this.assetLoader.loadAssets();

			this.assetManager.registerLoader(Bitmap.class, new BitmapLoader());
			this.assetManager.registerLoader(Texture.class, new TextureLoader());
			this.assetManager.registerLoader(Shader.class, new ShaderLoader());
			this.assetManager.registerLoader(Program.class, new ProgramLoader());
			this.assetManager.registerLoader(Mesh.class, new MeshLoader());
			this.assetManager.registerLoader(TextureAtlas.class, new TextureAtlasLoader());
		}

		@Override
		public void unload()
		{}
	}

	public abstract void load();

	public abstract void unload();
}