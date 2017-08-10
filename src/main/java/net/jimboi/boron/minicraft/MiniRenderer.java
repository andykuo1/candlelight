package net.jimboi.boron.minicraft;

import com.mojang.ld22.Game;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.window.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.asset.Asset;
import org.qsilver.util.ColorUtil;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.SpriteSheet;
import org.zilar.sprite.SpriteUtil;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 8/3/17.
 */
public class MiniRenderer
{
	private final Set<MiniRenderable> renderables = new HashSet<>();

	public final Camera camera;

	public Program program;
	public Mesh mesh;

	public Bitmap bmpFont;
	public Bitmap bmpBird;
	public Bitmap bmpIcons;
	public Bitmap bmpIcons2;
	public Bitmap bmpScreen;

	public Texture font;
	public Texture bird;
	public Texture icons;
	public Texture icons2;
	public Texture screen;

	public TextureAtlas atlasFont;
	public TextureAtlas atlasIcons;
	public TextureAtlas atlasIcons2;

	public SpriteSheet sheetFont;
	public SpriteSheet sheetIcons;
	public SpriteSheet sheetIcons2;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public MiniRenderer(Camera camera)
	{
		this.camera = camera;
	}

	public void load()
	{
		Shader vs = new Shader(new ResourceLocation("minicraft:simple.vsh"), GL20.GL_VERTEX_SHADER);
		Shader fs = new Shader(new ResourceLocation("minicraft:simple.fsh"), GL20.GL_FRAGMENT_SHADER);
		Program prgm = new Program();
		prgm.link(vs, fs);
		vs.close();
		fs.close();
		this.program = prgm;


		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
		this.mesh = ModelUtil.createStaticMesh(mb.bake(false, true));
		mb.clear();


		this.bmpBird = new Bitmap(new ResourceLocation("minicraft:bird.png"));
		this.bmpFont = new Bitmap(new ResourceLocation("minicraft:font.png"));
		this.bmpIcons = new Bitmap(new ResourceLocation("minicraft:icons.png"));
		this.bmpIcons2 = new Bitmap(new ResourceLocation("minicraft:icons2.png"));
		this.bmpScreen = new Bitmap(null, Game.WIDTH, Game.HEIGHT, Bitmap.Format.RGBA);


		this.bird = new Texture(this.bmpBird, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
		this.font = new Texture(this.bmpFont, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
		this.icons = new Texture(this.bmpIcons, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
		this.icons2 = new Texture(this.bmpIcons2, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
		this.screen = new Texture(Game.WIDTH, Game.HEIGHT, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE, Bitmap.Format.RGBA);


		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.font), 256, 256);
		{
			tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
			this.atlasFont = SpriteUtil.createTextureAtlas(tab.bake());
		}
		tab.clear();
		tab = new TextureAtlasBuilder(Asset.wrap(this.icons), 256, 256);
		{
			tab.addTileSheet(0, 0, 8, 8, 0, 0, 32, 32);
			this.atlasIcons = SpriteUtil.createTextureAtlas(tab.bake());
		}
		tab.clear();
		tab = new TextureAtlasBuilder(Asset.wrap(this.icons2), 256, 256);
		{
			tab.addTileSheet(0, 0, 32, 32, 0, 0, 32, 32);
			this.atlasIcons2 = SpriteUtil.createTextureAtlas(tab.bake());
		}
		tab.clear();


		this.sheetFont = new SpriteSheet(Asset.wrap(this.atlasFont), 0, this.atlasFont.length());
		this.sheetIcons = new SpriteSheet(Asset.wrap(this.atlasIcons), 0, this.atlasIcons.length());
		this.sheetIcons2 = new SpriteSheet(Asset.wrap(this.atlasIcons2), 0, this.atlasIcons2.length());
	}

	public void unload()
	{
		this.program.close();
		this.mesh.close();

		this.bmpBird.close();
		this.bmpFont.close();
		this.bmpIcons.close();
		this.bmpIcons2.close();
		this.bmpScreen.close();

		this.bird.close();
		this.font.close();
		this.icons.close();
		this.icons2.close();
		this.screen.close();
	}

	public void render()
	{
		Iterator<MiniRenderable> iterator = this.renderables.iterator();
		Matrix4fc u_model_view_projection;
		Matrix4fc u_model;
		final Matrix4fc u_view = camera.view();
		final Matrix4fc u_projection = camera.projection();

		int def_sampler = 0;

		final Program program = this.program;
		program.bind();
		{
			program.setUniform("u_projection", u_projection);
			program.setUniform("u_view", u_view);

			while (iterator.hasNext())
			{
				final MiniRenderable inst = iterator.next();
				if (!inst.isVisible()) continue;

				Mesh mesh = inst.getMesh();
				if (mesh == null) mesh = this.mesh;

				Sprite sprite = inst.getSprite();
				Texture texture = sprite.getTexture().getSource();

				int u_sampler = def_sampler;
				boolean u_transparency = inst.getTransparent();
				Vector4fc u_diffuse_color = inst.getColor();

				program.setUniform("u_transparency", u_transparency);
				program.setUniform("u_diffuse_color", u_diffuse_color);

				u_model = inst.getRenderTransformation(this.modelMatrix);
				u_model_view_projection = u_projection.mul(u_view, this.modelViewProjMatrix).mul(u_model, this.modelViewProjMatrix);

				program.setUniform("u_model", u_model);
				program.setUniform("u_model_view_projection", u_model_view_projection);

				mesh.bind();
				{
					if (texture != null)
					{
						u_sampler = 0;
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.bind();
					}

					program.setUniform("u_sampler", u_sampler);

					Vector2fc u_tex_offset = new Vector2f(sprite.getU(), sprite.getV());
					Vector2fc u_tex_scale = new Vector2f(sprite.getWidth(), sprite.getHeight());

					program.setUniform("u_tex_offset", u_tex_offset);
					program.setUniform("u_tex_scale", u_tex_scale);

					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

					if (texture != null)
					{
						texture.unbind();
					}
				}
				mesh.unbind();
			}
		}
		program.unbind();
	}

	public void drawScreen(int[] pixels, int width, int height)
	{
		byte[] arr = new byte[width * height * Bitmap.Format.RGBA.getChannel()];
		for(int i = pixels.length - 1; i >= 0; --i)
		{
			int j = (pixels.length - 1 - i) * Bitmap.Format.RGBA.getChannel();
			arr[j] = (byte) ColorUtil.getRed(pixels[i]);
			arr[j + 1] = (byte) ColorUtil.getGreen(pixels[i]);
			arr[j + 2] = (byte) ColorUtil.getBlue(pixels[i]);
			arr[j + 3] = (byte) 255;
		}

		ByteBuffer bb = BufferUtils.createByteBuffer(arr.length);
		bb.put(arr);
		bb.flip();

		this.screen.bind();
		{
			this.screen.update(bb, width, height, Bitmap.Format.RGBA);
		}
		this.screen.unbind();

		MiniRenderable renderable = new MiniRenderable();
		renderable.sprite = new Sprite(Asset.wrap(this.screen));
		renderable.isScreen = true;
		this.renderables.add(renderable);
	}

	public int[] getPixels(Bitmap bitmap)
	{
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		ByteBuffer bb = bitmap.getPixelBuffer();
		for(int i = pixels.length - 1; i >= 0; --i)
		{
			int j = (pixels.length - 1 - i) * bitmap.getFormat().getChannel();
			int r = bb.get(j);
			int g = bb.get(j + 1);
			int b = bb.get(j + 2);
			int a = bitmap.getFormat().getChannel() == 4 ? bb.get(j + 3) : 255;
			pixels[i] = (a << 24) + (r << 16) + (g << 8) + (b);
		}
		return pixels;
	}

	public MiniRenderable addRender(MiniRenderable render)
	{
		this.renderables.add(render);
		return render;
	}

	public void addRenderers(Collection<? extends MiniRenderable> renders)
	{
		this.renderables.addAll(renders);
	}

	public void removeRender(MiniRenderable render)
	{
		this.renderables.remove(render);
	}

	public void clearRenders()
	{
		this.renderables.clear();
	}
}
