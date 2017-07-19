package net.jimboi.boron.stage_a.woot;

import net.jimboi.boron.stage_a.shroom.RenderShroomBase;
import net.jimboi.boron.stage_a.shroom.Shroom;

import org.bstone.mogli.Mesh;
import org.joml.Vector2f;
import org.qsilver.render.RenderEngine;
import org.qsilver.resource.MeshLoader;
import org.zilar.meshbuilder.MeshBuilder;

/**
 * Created by Andy on 7/17/17.
 */
public class RenderWoot extends RenderShroomBase<SceneWoot>
{
	@Override
	protected void onLoad(RenderEngine renderEngine)
	{
		super.onLoad(renderEngine);

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(-0.5F, -0.5F), new Vector2f(0.5F, 0.5F), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Shroom.ENGINE.getAssetManager().registerAsset(Mesh.class, "billboard", new MeshLoader.MeshParameter(mb.bake(false, false)));
		mb.clear();
	}

	@Override
	protected Class<SceneWoot> getSceneClass()
	{
		return SceneWoot.class;
	}
}
