package net.jimboi.boron.stage_a.candle;

import net.jimboi.boron.base.RendererAssetBase;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.candle.world.WorldCandle;

import org.bstone.mogli.Mesh;
import org.bstone.render.RenderEngine;
import org.bstone.render.Renderable;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Vector2f;
import org.qsilver.resource.MeshLoader;
import org.qsilver.util.iterator.CastIterator;
import org.zilar.meshbuilder.MeshBuilder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/29/17.
 */
public class CandleRenderer extends RendererAssetBase
{
	private final Set<Renderable> renderables = new HashSet<>();

	public CandleRenderer(RenderEngine renderEngine)
	{
		super(renderEngine,
				new PerspectiveCamera(640, 480),
				Candle.getCandle().getAssetManager(),
				"candle",
				new SemanticVersion(0, 0, 0));
	}

	@Override
	protected void onLoad(RenderEngine renderEngine)
	{
		MeshBuilder mb = new MeshBuilder();

		//Create 2dc
		{
			mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
			Candle.getCandle().getAssetManager().registerAsset(Mesh.class, "2dc", new MeshLoader.MeshParameter(mb.bake(false, true)));
		}
		mb.clear();
	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{
		WorldCandle world = this.getScene().getWorld();
		world.getDungeonHandler().updateMesh(this);
	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{
		this.renderables.clear();
	}

	public void addCustomRenderable(Renderable renderable)
	{
		this.renderables.add(renderable);
	}

	public void removeCustomRenderable(Renderable renderable)
	{
		this.renderables.remove(renderable);
	}

	private final Set<EntityComponentRenderable> componentRenderables = new HashSet<>();
	private final Set<Renderable> simpleRenderables = new HashSet<>();

	@Override
	protected Iterator<Renderable> getSimpleRenderables()
	{
		this.componentRenderables.clear();
		this.getScene().getEntityManager().getSimilarComponents(EntityComponentRenderable.class, this.componentRenderables);
		this.simpleRenderables.clear();
		this.simpleRenderables.addAll(this.renderables);
		this.simpleRenderables.addAll(this.componentRenderables);
		return new CastIterator<>(this.simpleRenderables.iterator());
	}

	@Override
	protected SceneCandle getScene()
	{
		return (SceneCandle) super.getScene();
	}
}
