package net.jimboi.boron.stage_a.woot;

import net.jimboi.boron.stage_a.shroom.Shroom;
import net.jimboi.boron.stage_a.shroom.ShroomWorld;
import net.jimboi.boron.stage_a.shroom.component.EntityComponentRenderableBillboard;
import net.jimboi.boron.stage_a.shroom.component.LivingEntity;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.joml.Vector3fc;
import org.zilar.bounding.ShapeAABB;
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

	protected LivingEntity createPlayer(Vector3fc pos)
	{
		Transform3 transform = new Transform3();
		transform.position.set(pos);
		LivingEntity player = new LivingEntity(transform,
				this.getScene().getBoundingManager().createCollider(new ShapeAABB(pos.x(), pos.z(), 0.2F)),
				null);
		return player;
	}

	@Override
	public void create()
	{
		this.dungeonHandler = new DungeonHandler(this.getScene());
		this.dungeonHandler.generate(0, 45, 45);

		this.player = this.getScene().getLivingManager().add(this.createPlayer(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F)));
		this.getScene().getMainCameraController().setTarget((Transform3) this.player.getTransform(), this.player.getCollider());

		this.spawnLiving2D(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F), 1, 1, "font");
		this.spawnLiving2D(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F), 0.5F, 1, null);
	}

	@Override
	public void update(double delta)
	{

	}

	@Override
	public void destroy()
	{
	}

	public LivingEntity spawnLiving2D(Vector3fc pos, float w, float h, String textureID)
	{
		return this.spawnLiving2D(pos.x(), pos.y(), pos.z(), w, h, textureID);
	}

	public LivingEntity spawnLiving2D(float x, float y, float z, float w, float h, String textureID)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		transform.scale.set(w, h, 1);
		return this.scene.getLivingManager().add(new LivingEntity(
				transform,
				this.getScene().getBoundingManager().createCollider(new ShapeAABB(x, y, w / 2F)),
				new EntityComponentRenderableBillboard(this.getScene().getMainCameraTransform(),
						transform.derive3().setPosition(0, 0.5F, 0),
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
