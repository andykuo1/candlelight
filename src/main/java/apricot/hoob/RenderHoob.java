package apricot.hoob;

import apricot.base.OldGameEngine;
import apricot.base.OldRenderBase;
import apricot.base.assets.Assets;
import apricot.base.assets.resource.MeshLoader;
import apricot.base.assets.resource.TextureAtlasLoader;
import apricot.base.collision.CollisionRenderer;
import apricot.base.gui.GuiButton;
import apricot.base.gui.GuiFrame;
import apricot.base.gui.GuiMaterial;
import apricot.base.gui.GuiPanel;
import apricot.base.gui.GuiRenderer;
import apricot.base.gui.GuiText;
import apricot.base.gui.TextMesh;
import apricot.base.gui.base.Gui;
import apricot.base.gui.base.GuiManager;
import apricot.base.input.OldInputManager;
import apricot.base.render.OldRenderable;
import apricot.base.renderer.SimpleRenderer;
import apricot.base.renderer.property.OldPropertyTexture;
import apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import apricot.base.asset.Asset;
import apricot.base.render.OldRenderEngine;
import apricot.base.sprite.FontSheet;
import apricot.base.sprite.TextureAtlas;
import apricot.base.sprite.TextureAtlasBuilder;
import apricot.base.sprite.TextureAtlasData;

import apricot.bstone.camera.OrthographicCamera;
import apricot.bstone.camera.PerspectiveCamera;
import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Program;
import apricot.bstone.mogli.Texture;
import apricot.bstone.util.SemanticVersion;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import apricot.qsilver.util.iterator.CastIterator;
import apricot.zilar.meshbuilder.MeshBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Andy on 6/25/17.
 */
public class RenderHoob extends OldRenderBase
{
	private SimpleRenderer simpleRenderer;
	private CollisionRenderer collisionRenderer;
	private GuiRenderer guiRenderer;

	private GuiManager guiManager;

	private final Assets assets;

	public RenderHoob(OldRenderEngine renderEngine)
	{
		super(renderEngine, new PerspectiveCamera(640, 480));

		this.assets = Assets.create(OldGameEngine.ASSETMANAGER, "hoob", new SemanticVersion("0.0.0"));
	}

	@Override
	public void onRenderLoad(OldRenderEngine renderEngine)
	{
		System.out.println("RUNME!");
		this.assets.load();

		OldInputManager.registerMousePosX("mousex");
		OldInputManager.registerMousePosY("mousey");

		OldInputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		OldInputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		OldInputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		OldInputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		OldInputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		OldInputManager.registerKey("up", GLFW.GLFW_KEY_E);
		OldInputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		OldInputManager.registerKey("action", GLFW.GLFW_KEY_F);
		OldInputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		Asset<Texture> font = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "font");

		TextureAtlasBuilder t = new TextureAtlasBuilder(font, 256, 256);
		t.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		TextureAtlasData atlas = t.bake();
		t.clear();
		Asset<TextureAtlas> textureAtlas = OldGameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "font",
				new TextureAtlasLoader.TextureAtlasParameter(atlas));

		TextMesh.defaultFontSheet = new FontSheet(textureAtlas, 0, (char) 0, (char) 255);

		this.guiManager = new GuiManager(new OrthographicCamera(640, 480), OldGameEngine.WINDOW.getCurrentViewPort());
		OldGameEngine.INPUTENGINE.addInputLayer(this.guiManager);

		Gui frame = new GuiFrame();
		frame.setSize(9, 10);

		Gui panel = new GuiPanel();
		panel.setPosition(0.5F, 0.5F);
		panel.setSize(8, 9);
		frame.addChild(panel);

		Gui button = new GuiButton((gui) -> System.out.println("BOO!"));
		button.setPosition(1, 2);
		button.setSize(6, 1);
		panel.addChild(button);

		Gui text = new GuiText("Hello");
		text.setSize(6, 1);
		button.addChild(text);

		button = new GuiButton((gui) -> System.out.println("BOO 2!"));
		button.setPosition(1, 4);
		button.setSize(2, 1);
		panel.addChild(button);

		button = new GuiButton((gui) -> System.out.println("BOO 3!"));
		button.setPosition(5, 4);
		button.setSize(2, 1);
		button.setEnabled(false);
		panel.addChild(button);

		GuiMaterial mat = new GuiMaterial(this.scene.getMaterialManager().createMaterial(new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny"))));
		mat.setPosition(0.5F, 0.5F);
		mat.setSize(1, 1);
		panel.addChild(mat);

		this.guiManager.addGui(frame);









		this.simpleRenderer = new SimpleRenderer(renderEngine, OldGameEngine.ASSETMANAGER.getAsset(Program.class, "simple"), this.getCamera(), () -> new CastIterator<>(this.getScene().getEntityManager().getComponents(EntityComponentRenderable.class, new HashSet<>()).iterator()));
		this.simpleRenderer.start();
		this.collisionRenderer = new CollisionRenderer(renderEngine, ((SceneHoob) this.scene).getCollisionManager(), OldGameEngine.ASSETMANAGER.getAsset(Program.class, "wireframe"));
		this.collisionRenderer.start();
		this.guiRenderer = new GuiRenderer(renderEngine, this.guiManager, OldGameEngine.ASSETMANAGER.getAsset(Program.class, "simple"));
		this.guiRenderer.start();

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		OldGameEngine.ASSETMANAGER.registerAsset(Mesh.class, "quad",
				new MeshLoader.MeshParameter(mb.bake(false, true)));
		mb.clear();

		for(int i = 0; i < 20; ++i)
		{
			for(int j = 0; j < 20; ++j)
			{
				mb.addPlane(new Vector2f(i, j), new Vector2f(i + 1, j + 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
			}
		}
		OldGameEngine.ASSETMANAGER.registerAsset(Mesh.class, "ground", new MeshLoader.MeshParameter(mb.bake(true, true)));
		mb.clear();

		Asset<Texture> crate = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "crate");
		Asset<Texture> bunny = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny");

		TextureAtlasBuilder tab = new TextureAtlasBuilder(bunny, 144, 48);
		{
			tab.addHorizontalStrip(0, 0, 48, 48, 0, 3);
			OldGameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "bunny",
					new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
		}
		tab.clear();

		tab = new TextureAtlasBuilder(crate, 256, 256);
		{
			tab.addNineSheet(32, 32, 32, 32);
			OldGameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "button",
					new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
		}
		tab.clear();
	}

	@Override
	public void onRender(OldRenderEngine renderEngine)
	{
		this.guiManager.update();

		Collection<EntityComponentRenderable> renderables = this.getScene().getEntityManager().getComponents(EntityComponentRenderable.class, new HashSet<>());

		Iterator<OldRenderable> iter = new CastIterator<>(renderables.iterator());
		this.simpleRenderer.render(this.getCamera(), iter);

		this.collisionRenderer.render(this.getCamera(), new Matrix4f());

		this.guiRenderer.render();
	}

	@Override
	public void onRenderUnload(OldRenderEngine renderEngine)
	{
		this.guiManager.destroy();
		OldGameEngine.INPUTENGINE.removeInputLayer(this.guiManager);
		this.collisionRenderer.stop();
		TextMesh.clear();
	}
}
