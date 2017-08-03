package net.jimboi.boron.stage_a.tung;

import net.jimboi.boron.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.base.livingentity.LivingEntity;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneService;
import org.qsilver.util.map2d.IntMap;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyTexture;

import java.util.Random;

/**
 * Created by Andy on 7/23/17.
 */
public class WorldTung extends SceneService implements Scene.OnSceneUpdateListener
{
	protected final SceneTung scene;

	private EntityPlayer player;
	private IntMap tilemap = new IntMap(16, 16);

	public WorldTung(SceneTung scene)
	{
		this.scene = scene;
	}

	@Override
	protected void onStart(Scene handler)
	{
		handler.onSceneUpdate.addListener(this);

		Random rand = new Random();
		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.tilemap.set(i, 0);
		}

		this.tilemap.set(1, 1, 1);
		this.tilemap.set(1, 2, 1);

		Transform3 transform = new Transform3();
		this.player = new EntityPlayer(transform, this.createRenderable(transform, "bunny"));
		this.scene.spawn(this.player);

		this.scene.getMainCameraController().setTarget(transform);

		this.createLiving(0, 0, "bunny");
		this.createLiving(10, 10, "bunny");
	}

	protected LivingEntity createLiving(float x, float y, String textureID)
	{
		Transform3 transform = new Transform3();
		transform.setPosition(x, y, 0);
		return this.scene.spawn(new LivingEntity(transform, this.createRenderable(transform, textureID)));
	}

	protected EntityComponentRenderable createRenderable(Transform3 transform, String textureID)
	{
		return new EntityComponentRenderable(transform,
				new Model(Tung.ENGINE.getAssetManager().getAsset(Mesh.class, "2d"),
						this.getScene().getMaterialManager().createMaterial(
								new PropertyTexture(
										Tung.ENGINE.getAssetManager().getAsset(Texture.class, textureID)
								)
						),
						"simple"));
	}

	@Override
	protected void onStop(Scene handler)
	{
		handler.onSceneUpdate.deleteListener(this);
	}

	@Override
	public void onSceneUpdate(double delta)
	{

	}

	public IntMap getTileMap()
	{
		return this.tilemap;
	}

	public SceneTung getScene()
	{
		return this.scene;
	}
}
