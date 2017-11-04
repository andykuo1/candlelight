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

import org.bstone.asset.AssetManager;
import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.adapter.InputAdapter;
import org.bstone.livingentity.LivingEntityManager;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;
import org.bstone.util.gridmap.IntMap;
import org.lwjgl.glfw.GLFW;
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
	private LivingEntityManager entityManager;

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
		final InputContext context = inputs.getDefaultContext();

		this.collisionManager = new CollisionBoxManager();
		this.entityManager = new LivingEntityManager();

		this.colliders = new HashSet<>();

		this.camera = new PerspectiveCamera(0, 0, 10, 640, 480);
		this.cameraController = new FirstPersonCameraController((PerspectiveCamera) this.camera);
		context.addListener(0, this.cameraController);

		context.registerEvent("mousex", inputs.getMouse().getCursorX()::getRange);
		context.registerEvent("mousey", inputs.getMouse().getCursorY()::getRange);

		context.registerEvent("mouseleft", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)::getAction);
		context.registerEvent("mouseright", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT)::getAction);

		context.registerEvent("mouselock", inputs.getKeyboard().getButton(GLFW.GLFW_KEY_P)::getAction);

		context.registerEvent("exit", inputs.getKeyboard().getButton(GLFW.GLFW_KEY_ESCAPE)::getState);

		context.registerEvent("forward",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_W)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_S)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_UP)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)::getState
				));

		context.registerEvent("up",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_E)::getState
				));

		context.registerEvent("right",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_D)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_A)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)::getState
				));

		context.registerEvent("sprint",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT_SHIFT)::getState);

		context.registerEvent("action",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_F)::getState);

		DungeonBuilder db = new MazeDungeonBuilder(0, 45, 45);
		DungeonData data = db.bake();
		this.dungeonMap = data.getTiles();
		this.dungeonCollider = () -> Dungeon.createBoundingBoxFromMap(this.dungeonMap);

		{
			final AssetManager assets = Lantern.getLantern().getAssetManager();
			MeshData mesh = Dungeon.createMeshFromMap(this.dungeonMap, assets.getAsset("texture_atlas", "font"));
			assets.cacheResource("mesh", "dungeon", ModelUtil.createStaticMesh(mesh));
		}

		this.player = this.entityManager.addLivingEntity(new EntityPlayer());

		this.entityManager.addLivingEntity(new EntityCrate());
	}

	@Override
	protected void onSceneUpdate()
	{
		this.cameraController.update();
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
		final InputContext context = Lantern.getLantern().getInputEngine().getDefaultContext();

		context.removeListener(this.cameraController);

		this.entityManager.clear();
	}

	public LivingEntityManager getEntityManager()
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
