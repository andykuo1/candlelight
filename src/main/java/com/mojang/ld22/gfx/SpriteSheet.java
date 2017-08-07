package com.mojang.ld22.gfx;

import net.jimboi.boron.minicraft.MiniCraft;

import org.bstone.mogli.Bitmap;

public class SpriteSheet {
	public int width, height;
	public int[] pixels;

	public SpriteSheet(Bitmap bitmap)
	{
		this.pixels = MiniCraft.getMiniCraft().renderer.getPixels(bitmap);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (pixels[i] & 0xff) / 64;
		}
	}
}