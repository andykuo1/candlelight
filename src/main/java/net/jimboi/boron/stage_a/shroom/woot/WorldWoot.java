package net.jimboi.boron.stage_a.shroom.woot;

import net.jimboi.boron.base.livingentity.EntityComponentRenderableBillboard;
import net.jimboi.boron.stage_a.shroom.Shroom;
import net.jimboi.boron.stage_a.shroom.ShroomWorld;
import net.jimboi.boron.stage_a.shroom.component.OldLivingEntity;
import net.jimboi.boron.stage_a.shroom.woot.collision.CollisionData;
import net.jimboi.boron.stage_a.shroom.woot.collision.CollisionManager;
import net.jimboi.boron.stage_a.shroom.woot.collision.Shape;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
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
	private OldLivingEntity cursor;
	private OldLivingEntity player;

	private CollisionManager collisionManager;

	public WorldWoot(SceneWoot scene)
	{
		super(scene);

		this.collisionManager = new CollisionManager();
	}

	protected OldLivingEntity createPlayer(Vector3fc pos)
	{
		Transform3 transform = new Transform3();
		transform.position.set(pos);
		OldLivingEntity player = new OldLivingEntity(
				this,
				transform,
				this.collisionManager.createCollider(new Shape.AABB(pos.x(), pos.z(), 0.2F)),
				null);
		return player;
	}

	protected OldLivingEntity createCursor()
	{
		Transform3 transform = new Transform3();
		transform.scale.set(0.01F);
		OldLivingEntity cursor = new OldLivingEntity(
				this,
				transform,
				null,
				new EntityComponentRenderableBillboard(
						this.getScene().getMainCameraTransform(),
						transform,
						new Model(Shroom.ENGINE.getAssetManager().getAsset(Mesh.class, "billboard"),
								this.getScene().getMaterialManager().createMaterial(
										new PropertyDiffuse().setDiffuseColor(0xFF0000)),
								"simple")
				))
		{
			private Shape.Segment shape;

			@Override
			public boolean onCreate()
			{
				this.shape = new Shape.Segment(this.getTransform().position3().x(), this.getTransform().position3().z(), 0, 0);
				return super.onCreate();
			}

			@Override
			public void onUpdate(double delta)
			{
				super.onUpdate(delta);

				Transform3c transform = this.getTransform();
				ViewPort viewport = getScene().getWorldSpace().getViewport();
				Vector3f vec = getScene().getWorldSpace().getVectorFromScreen(viewport.getX() + viewport.getWidth() / 2F, viewport.getY() + viewport.getHeight() / 2F, new Vector3f());

				this.shape.setCenter(transform.position3().x(), transform.position3().z());
				this.shape.setDelta(vec.x(), vec.z());

				CollisionData collisionData = this.getWorld().collisionManager.getCollisionSolver().checkNearestCollision(this.shape);

				if (collisionData != null)
				{
					//System.out.println("BOO");
					vec.set(collisionData.deltaX, vec.y(), collisionData.deltaY);
				}

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

	public OldLivingEntity spawnLiving2D(Vector3fc pos, float w, float h, String textureID)
	{
		return this.spawnLiving2D(pos.x(), pos.y(), pos.z(), w, h, textureID);
	}

	public OldLivingEntity spawnLiving2D(float x, float y, float z, float w, float h, String textureID)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		transform.scale.set(w, h, 1);
		return this.scene.getLivingManager().add(new OldLivingEntity(
				this,
				transform,
				this.collisionManager.createCollider(new Shape.AABB(x, y, w / 2F)),
				new EntityComponentRenderableBillboard(this.getScene().getMainCameraTransform(),
						transform.derive3().setPosition(0, 0.5F, 0),
						new Model(Shroom.ENGINE.getAssetManager().getAsset(Mesh.class, "billboard"),
								this.getScene().getMaterialManager().createMaterial(
										textureID != null ? new PropertyTexture(Shroom.ENGINE.getAssetManager().getAsset(Texture.class, textureID)) : new PropertyDiffuse().setDiffuseColor(0xFF00FF)),
								"simple"))));
	}

	public CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}

	@Override
	public SceneWoot getScene()
	{
		return (SceneWoot) super.getScene();
	}

	public OldLivingEntity getPlayer()
	{
		return this.player;
	}
}
