package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.canary.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.canary.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.canary.stage_a.lantern.Lantern;
import net.jimboi.canary.stage_a.lantern.scene_main.cameracontroller.FirstPersonCameraController;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentBounding;
import net.jimboi.canary.stage_a.lantern.scene_main.component.ComponentTransform;
import net.jimboi.canary.stage_a.lantern.scene_main.dungeon.Dungeon;
import net.jimboi.canary.stage_a.lantern.scene_main.entity.EntityCrate;
import net.jimboi.canary.stage_a.lantern.scene_main.entity.EntityPlayer;

import org.bstone.application.game.GameInputs;
import org.bstone.asset.AssetManager;
import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.gameobject.GameObjectManager;
import org.bstone.input.InputEngine;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;
import org.bstone.util.grid.IntMap;
import org.zilar.dungeon.DungeonBuilder;
import org.zilar.dungeon.DungeonData;
import org.zilar.dungeon.maze.MazeDungeonBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 11/2/17.
 */
public class SceneMain extends Scene
{
	private CollisionBoxManager collisionManager;
	private GameObjectManager entityManager;

	private Set<BoxCollider> colliders;

	private Camera camera;
	private FirstPersonCameraController cameraController;

	private IntMap dungeonMap;
	private BoxCollider dungeonCollider;

	private EntityPlayer player;

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		final InputEngine inputs = Lantern.getLantern().getInputEngine();

		this.collisionManager = new CollisionBoxManager();
		this.entityManager = new GameObjectManager();

		this.colliders = new HashSet<>();

		this.camera = new PerspectiveCamera(0, 0, 10, 640, 480);
		this.cameraController = new FirstPersonCameraController((PerspectiveCamera) this.camera);

		GameInputs.loadBaseInputs(inputs);

		DungeonBuilder db = new MazeDungeonBuilder(0, 45, 45);
		DungeonData data = db.bake();
		this.dungeonMap = data.getTiles();
		this.dungeonCollider = () -> Dungeon.createBoundingBoxFromMap(this.dungeonMap);

		{
			final AssetManager assets = Lantern.getLantern().getAssetManager();
			MeshData mesh = Dungeon.createMeshFromMap(this.dungeonMap, assets.getAsset("texture_atlas", "font"));
			assets.cacheResource("mesh", "dungeon", ModelUtil.createStaticMesh(mesh));
		}

		this.player = this.entityManager.addGameObject(new EntityPlayer());

		this.entityManager.addGameObject(new EntityCrate());

		this.entityManager.flushCreatedObjects();
	}

	@Override
	protected void onSceneUpdate()
	{
		this.cameraController.update(Lantern.getLantern().getInputEngine());
		this.cameraController.updateTransform(this.player.getComponent(ComponentTransform.class).transform);

		this.colliders.clear();
		this.entityManager.getEntityManager().getComponents(ComponentBounding.class, this.colliders);
		this.colliders.add(this.dungeonCollider);

		this.collisionManager.update(this.colliders);
		this.entityManager.update();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.entityManager.clear();
	}

	public GameObjectManager getEntityManager()
	{
		return this.entityManager;
	}

	public CollisionBoxManager getCollisionManager()
	{
		return this.collisionManager;
	}

	public Set<BoxCollider> getColliders()
	{
		return this.colliders;
	}

	public Camera getCamera()
	{
		return this.camera;
	}
}
