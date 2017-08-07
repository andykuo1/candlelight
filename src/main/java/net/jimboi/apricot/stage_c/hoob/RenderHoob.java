package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.OldRenderBase;
import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.base.renderer.SimpleRenderer;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.Renderable;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.asset.Asset;
import org.qsilver.resource.MeshLoader;
import org.qsilver.resource.TextureAtlasLoader;
import org.qsilver.util.iterator.CastIterator;
import org.zilar.base.Assets;
import org.zilar.collision.CollisionRenderer;
import org.zilar.gui.GuiButton;
import org.zilar.gui.GuiFrame;
import org.zilar.gui.GuiMaterial;
import org.zilar.gui.GuiPanel;
import org.zilar.gui.GuiRenderer;
import org.zilar.gui.GuiText;
import org.zilar.gui.TextMesh;
import org.zilar.gui.base.Gui;
import org.zilar.gui.base.GuiManager;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.sprite.FontSheet;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;
import org.zilar.sprite.TextureAtlasData;

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

	public RenderHoob(RenderEngine renderEngine)
	{
		super(renderEngine, new PerspectiveCamera(640, 480));

		this.assets = Assets.create(OldGameEngine.ASSETMANAGER, "hoob", new SemanticVersion("0.0.0"));
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
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

		GuiMaterial mat = new GuiMaterial(this.scene.getMaterialManager().createMaterial(new PropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny"))));
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
	public void onRender(RenderEngine renderEngine)
	{
		this.guiManager.update();

		Collection<EntityComponentRenderable> renderables = this.getScene().getEntityManager().getComponents(EntityComponentRenderable.class, new HashSet<>());

		Iterator<Renderable> iter = new CastIterator<>(renderables.iterator());
		this.simpleRenderer.render(this.getCamera(), iter);

		this.collisionRenderer.render(this.getCamera(), new Matrix4f());

		this.guiRenderer.render();
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.guiManager.destroy();
		OldGameEngine.INPUTENGINE.removeInputLayer(this.guiManager);
		this.collisionRenderer.stop();
		TextMesh.clear();
	}
}
