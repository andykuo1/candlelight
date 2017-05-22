package org.bstone.util;

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

	public static int colorToHex(Color color)
	{
		return rgbToHex(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * Get hex from RGB
	 */
	public static int rgbToHex(int red, int green, int blue)
	{
		return ((red & 0x0FF) << 16) | ((green & 0x0FF) << 8) | (blue & 0x0FF);
	}

	/**
	 * Get normalized red from hex
	 */
	public static float getRedf(int parHex)
	{
		return ColorUtil.getNormalizedChannel(ColorUtil.red(parHex));
	}

	/**
	 * Get red from hex
	 */
	public static int red(int hex)
	{
		return (hex >> 16) & 0xFF;
	}

	/**
	 * Get normalized color channel out of 255
	 */
	public static float getNormalizedChannel(int parChannel)
	{
		return parChannel / 255F;
	}

	/**
	 * Get normalized green from hex
	 */
	public static float getGreenf(int parHex)
	{
		return ColorUtil.getNormalizedChannel(ColorUtil.green(parHex));
	}

	/**
	 * Get green from hex
	 */
	public static int green(int hex)
	{
		return (hex >> 8) & 0xFF;
	}

	/**
	 * Get normalized blue from hex
	 */
	public static float getBluef(int parHex)
	{
		return ColorUtil.getNormalizedChannel(ColorUtil.blue(parHex));
	}

	/**
	 * Get blue from hex
	 */
	public static int blue(int parHex)
	{
		return parHex & 0xFF;
	}

	/**
	 * Get normalized alpha from hex
	 */
	public static float getAlphaf(int parHex)
	{
		return ColorUtil.getNormalizedChannel(ColorUtil.getAlpha(parHex));
	}

	/**
	 * Get alpha from hex
	 */
	public static int getAlpha(int parHex)
	{
		return (parHex >> 24) & 0xFF;
	}

	/**
	 * Get normalized red from color
	 */
	public static float getRedf(Color color)
	{
		return ColorUtil.getNormalizedChannel(color.getRed());
	}

	/**
	 * Get normalized green from color
	 */
	public static float getGreenf(Color color)
	{
		return ColorUtil.getNormalizedChannel(color.getGreen());
	}

	/**
	 * Get normalized blue from color
	 */
	public static float getBluef(Color color)
	{
		return ColorUtil.getNormalizedChannel(color.getBlue());
	}

	/**
	 * Get normalized alpha from color
	 */
	public static float getAlphaf(Color color)
	{
		return ColorUtil.getNormalizedChannel(color.getAlpha());
	}

	/**
	 * Get random color
	 */
	public static Color getRandomColor(Random random, float saturation, float brightness)
	{
		float hue = random.nextFloat();
		return hexToColor(ColorUtil.HSBtoRGB(hue, saturation, brightness));
	}

	/**
	 * Get Color from hex
	 */
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
