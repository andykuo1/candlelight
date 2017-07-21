package net.jimboi.boron.stage_a.woot;

import net.jimboi.apricot.stage_c.hoob.collision.Shape;
import net.jimboi.boron.stage_a.shroom.Shroom;
import net.jimboi.boron.stage_a.shroom.ShroomWorld;
import net.jimboi.boron.stage_a.shroom.component.EntityComponentRenderableBillboard;
import net.jimboi.boron.stage_a.shroom.component.LivingEntity;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.bstone.window.view.ViewPort;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyDiffuse;
import org.zilar.renderer.property.PropertyTexture;

/**
 * Created by Andy on 7/17/17.
 */
public class WorldWoot extends ShroomWorld
{
	private DungeonHandler dungeonHandler;
	private LivingEntity cursor;
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
				this.getScene().getCollisionManager().createCollider(new Shape.Circle(pos.x(), pos.z(), 0.2F)),
				null);
		return player;
	}

	protected LivingEntity createCursor()
	{
		Transform3 transform = new Transform3();
		transform.scale.set(0.01F);
		LivingEntity cursor = new LivingEntity(transform,
				this.getScene().getCollisionManager().createCollider(new Shape.Point(transform.position.x(), transform.position.z())),
				new EntityComponentRenderableBillboard(
						this.getScene().getMainCameraTransform(),
						transform,
						new Model(Shroom.ENGINE.getAssetManager().getAsset(Mesh.class, "billboard"),
								this.getScene().getMaterialManager().createMaterial(
										new PropertyDiffuse().setDiffuseColor(0xFF0000)),
								"simple")
				))
		{
			@Override
			public void onUpdate(double delta)
			{
				super.onUpdate(delta);

				ViewPort viewport = getScene().getWorldSpace().getViewport();
				Vector3f vec = getScene().getWorldSpace().getVectorFromScreen(viewport.getX() + viewport.getWidth() / 2F, viewport.getY() + viewport.getHeight() / 2F, new Vector3f());
				((Transform3) this.getTransform()).position.set(player.getTransform().position3()).add(vec);
			}
		};
		return cursor;
	}

	@Override
	public void create()
	{
		this.dungeonHandler = new DungeonHandler(this.getScene());
		this.dungeonHandler.generate(0, 45, 45);

		this.player = this.getScene().getLivingManager().add(this.createPlayer(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F)));
		this.cursor = this.getScene().getLivingManager().add(this.createCursor());
		this.getScene().getMainCameraController().setTarget((Transform3) this.player.getTransform(), this.player.getCollider());

		this.spawnLiving2D(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F), 1, 1, "font");
		this.spawnLiving2D(this.dungeonHandler.getRandomTilePos(false).add(0.5F, 0, 0.5F), 0.5F, 1, null);
	}

	@Override
	public void update(double delta)
	{
		ViewPort viewport = this.getScene().getWorldSpace().getViewport();
		Vector3f vec = this.getScene().getWorldSpace().getVectorFromScreen(viewport.getX() + viewport.getWidth() / 2F, viewport.getY() + viewport.getHeight() / 2F, new Vector3f());
		((Transform3) this.cursor.getTransform()).position.set(this.player.getTransform().position3()).add(vec);
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
				this.getScene().getCollisionManager().createCollider(new Shape.AABB(x, y, w / 2F)),
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
