package net.jimboi.boron.stage_a.woot;

import net.jimboi.boron.stage_a.shroom.Shroom;
import net.jimboi.boron.stage_a.shroom.ShroomWorld;
import net.jimboi.boron.stage_a.shroom.component.EntityComponentRenderableBillboard;
import net.jimboi.boron.stage_a.shroom.component.LivingEntity;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyDiffuse;
import org.zilar.renderer.property.PropertyTexture;

/**
 * Created by Andy on 7/17/17.
 */
public class WorldWoot extends ShroomWorld
{
	private DungeonHandler dungeonHandler;

	private LivingEntity player;

	public WorldWoot(SceneWoot scene)
	{
		super(scene);
	}

	protected LivingEntity createPlayer()
	{
		LivingEntity player = new LivingEntity(new Transform3(), null);
		return player;
	}

	@Override
	public void create()
	{
		this.dungeonHandler = new DungeonHandler(this.getScene());
		this.dungeonHandler.generate(1, 45, 45);

		this.player = this.createPlayer();

		this.getScene().getMainCameraController().setTarget((Transform3) this.player.getTransform());

		this.spawnLiving2D(0, 0, 0, 1, 1, "font");
		this.spawnLiving2D(2, 0, 2, 0.5F, 1, null);
	}

	@Override
	public void update(double delta)
	{

	}

	@Override
	public void destroy()
	{
	}

	public LivingEntity spawnLiving2D(float x, float y, float z, float w, float h, String textureID)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		transform.scale.set(w, h, 1);
		return this.scene.getLivingManager().add(new LivingEntity(transform,
				new EntityComponentRenderableBillboard(this.getScene().getMainCameraTransform(),
						transform.derive().setOffset(0, 0.5F, 0),
						new Model(Shroom.ENGINE.getAssetManager().getAsset(Mesh.class, "billboard"),
								this.getScene().getMaterialManager().createMaterial(
										textureID != null ? new PropertyTexture(Shroom.ENGINE.getAssetManager().getAsset(Texture.class, textureID)) : new PropertyDiffuse().setDiffuseColor(0xFF00FF)),
								"simple"))));
	}

	@Override
	public SceneWoot getScene()
	{
		return (SceneWoot) super.getScene();
	}

	public LivingEntity getPlayer()
	{
		return this.player;
	}
}
