package net.jimboi.boron.stage_a.tung;

import net.jimboi.boron.base.RenderAssetBase;
import net.jimboi.boron.base.livingentity.EntityComponentRenderable;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.Renderable;
import org.qsilver.resource.MeshLoader;
import org.qsilver.util.iterator.CastIterator;
import org.qsilver.util.map2d.IntMap;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyTexture;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/21/17.
 */
public class RenderTung extends RenderAssetBase
{
	private Set<Renderable> renderables = new HashSet<>();
	private Set<EntityComponentRenderable> componentRenderables = new HashSet<>();
	private Set<Renderable> simpleRenderables = new HashSet<>();

	public RenderTung()
	{
		super(new PerspectiveCamera(640, 480), Tung.ENGINE.getAssetManager(), "tung", new SemanticVersion(0, 0, 0));
	}

	@Override
	protected void onLoad(RenderEngine renderEngine)
	{
		MeshData worldMeshData = createMeshFromMap(this.getScene().getWorld().getTileMap(), this.getAssetManager().getAsset(TextureAtlas.class, "font"));
		Asset<Mesh> mesh = this.getAssetManager().registerAsset(Mesh.class, "world", new MeshLoader.MeshParameter(worldMeshData, false));
		final Model worldModel = new Model(mesh, this.getScene().getMaterialManager().createMaterial(new PropertyTexture(this.getAssetManager().getAsset(Texture.class, "font"))), "simple");
		this.renderables.add(new Renderable()
		{
			@Override
			public Model getModel()
			{
				return worldModel;
			}

			@Override
			public Matrix4f getRenderTransformation(Matrix4f dst)
			{
				return dst.translation(0, 0, -1F);
			}

			@Override
			public boolean isVisible()
			{
				return true;
			}
		});
	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{
		WorldTung world = this.getScene().getWorld();
		MeshData worldMeshData = createMeshFromMap(world.getTileMap(), this.getAssetManager().getAsset(TextureAtlas.class, "font"));
		Asset<Mesh> worldMesh = this.getAssetManager().getAsset(Mesh.class, "world");
		ModelUtil.updateMesh(worldMesh.getSource(), worldMeshData);
	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{

	}

	@Override
	protected Iterator<Renderable> getSimpleRenderables()
	{
		this.componentRenderables.clear();
		this.getScene().getEntityManager().getSimilarComponents(EntityComponentRenderable.class, this.componentRenderables);

		this.simpleRenderables.clear();
		this.simpleRenderables.addAll(this.renderables);
		this.simpleRenderables.addAll(componentRenderables);
		return new CastIterator<>(this.simpleRenderables.iterator());
	}

	@Override
	protected SceneTung getScene()
	{
		return (SceneTung) super.getScene();
	}

	private static MeshData createMeshFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite;
		Vector2f vec = new Vector2f();

		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
			{
				Vector2fc wallPos = new Vector2f(x, y);

				int tile = map.get(x, y);
				sprite = textureAtlas.getSource().getSprite(tile);
				texTopLeft.set(sprite.getU(), sprite.getV());
				texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

				mb.addPlane(wallPos, wallPos.add(1, 1, vec), 0, texTopLeft, texBotRight);
			}
		}

		return mb.bake(false, false);
	}
}
