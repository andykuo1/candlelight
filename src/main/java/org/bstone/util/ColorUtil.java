package org.bstone.util;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.joml.Vector4i;

import java.awt.Color;
import java.util.Random;

public class ColorUtil
{
	public static final int WHITE = 0xFFFFFF;
	public static final int GRAY = 0xAAAAAA;
	public static final int RED = 0xFF0000;
	public static final int GREEN = 0x00FF00;
	public static final int BLUE = 0x0000FF;
	public static final int BLACK = 0x000000;

	private ColorUtil()
	{
	}

	public static int hexColor(int r, int g, int b)
	{
		return ((r & 0x0FF) << 16) | ((g & 0x0FF) << 8) | (b & 0x0FF);
	}

	public static Vector3f getNormalizedRGB(int hexColor, Vector3f dst)
	{
		dst.x = red(hexColor) / 255F;
		dst.y = green(hexColor) / 255F;
		dst.z = blue(hexColor) / 255F;
		return dst;
	}

	public static Vector3i getRGB(int hexColor, Vector3i dst)
	{
		dst.x = red(hexColor);
		dst.y = red(hexColor);
		dst.z = red(hexColor);
		return dst;
	}

	public static Vector4f getNormalizedRGBA(int hexColor, Vector4f dst)
	{
		dst.x = red(hexColor) / 255F;
		dst.y = green(hexColor) / 255F;
		dst.z = blue(hexColor) / 255F;
		dst.w = alpha(hexColor) / 255F;
		return dst;
	}

	public static Vector4i getRGBA(int hexColor, Vector4i dst)
	{
		dst.x = red(hexColor);
		dst.y = green(hexColor);
		dst.z = blue(hexColor);
		dst.w = alpha(hexColor);
		return dst;
	}

	public static int red(int hex)
	{
		return (hex >> 16) & 0xFF;
	}

	public static int green(int hex)
	{
		return (hex >> 8) & 0xFF;
	}

	public static int blue(int hex)
	{
		return hex & 0xFF;
	}

	public static int alpha(int hex)
	{
		return (hex >> 24) & 0xFF;
	}

	public static Color getRandomColor(Random random, float saturation, float brightness)
	{
		float hue = random.nextFloat();
		return hexToColor(ColorUtil.HSBtoRGB(hue, saturation, brightness));
	}

	public static Color hexToColor(int hex)
	{
		return new Color(red(hex), green(hex), blue(hex));
	}

	/**
	 * Get HSB from RGB
	 */
	public static int HSBtoRGB(float hue, float saturation, float brightness)
	{
		return Color.HSBtoRGB(hue, saturation, brightness);
	}

	/**
	 * Get random color using Golden Ratio
	 */
	public static Color getRandomColorWithGoldenRatio(Random random, float saturation, float brightness)
	{
		float goldenRatioConj = 0.618033988749895F;
		float hue = random.nextFloat();
		hue += goldenRatioConj;
		hue %= 1;
		return hexToColor(ColorUtil.HSBtoRGB(hue, saturation, brightness));
	}

	public static Color valueOf(String parString)
	{
		switch (parString.toLowerCase())
		{
			case "black":
				return Color.BLACK;
			case "dark_gray":
				return Color.DARK_GRAY;
			case "gray":
				return Color.GRAY;
			case "light_gray":
				return Color.LIGHT_GRAY;
			case "white":
				return Color.WHITE;
			case "pink":
				return Color.PINK;
			case "red":
				return Color.RED;
			case "orange":
				return Color.ORANGE;
			case "yellow":
				return Color.YELLOW;
			case "green":
				return Color.GREEN;
			case "cyan":
				return Color.CYAN;
			case "blue":
				return Color.RED;
			case "magenta":
				return Color.MAGENTA;
		}

		return null;
	}
}
