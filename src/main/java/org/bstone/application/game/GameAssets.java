package org.bstone.application.game;

import org.bstone.asset.AssetManager;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 12/2/17.
 */
public class GameAssets
{
	public static void loadBaseAssets(AssetManager assets)
	{
		assets.registerResourceLocation("program.simple", new ResourceLocation("base:simple.prg"));
		assets.registerResourceLocation("vertex_shader.simple", new ResourceLocation("base:simple.vsh"));
		assets.registerResourceLocation("fragment_shader.simple", new ResourceLocation("base:simple.fsh"));

		assets.registerResourceLocation("mesh.cube", new ResourceLocation("base:cube.obj"));
		assets.registerResourceLocation("mesh.sphere", new ResourceLocation("base:sphere.obj"));
		assets.registerResourceLocation("mesh.quad", new ResourceLocation("base:quad.obj"));
		assets.registerResourceLocation("mesh.rect", new ResourceLocation("base:rect.obj"));
		assets.registerResourceLocation("mesh.circle", new ResourceLocation("base:circle.obj"));

		assets.registerResourceLocation("bitmap.font", new ResourceLocation("base:font.png"));
		assets.registerResourceLocation("texture.font", new ResourceLocation("base:font.tex"));

		assets.registerResourceLocation("bitmap.wooden_crate", new ResourceLocation("base:wooden_crate.jpg"));
		assets.registerResourceLocation("texture.wooden_crate", new ResourceLocation("base:wooden_crate.tex"));
	}
}
