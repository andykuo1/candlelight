package canary.bstone.util;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.joml.Vector4i;

import java.util.Random;

/**
 * Created by Andy on 8/5/17.
 */
public class ColorUtil
{
	public static final int WHITE = 0xFFFFFF;
	public static final int GRAY = 0x888888;
	public static final int RED = 0xFF0000;
	public static final int GREEN = 0x00FF00;
	public static final int BLUE = 0x0000FF;
	public static final int BLACK = 0x000000;

	public static int getColor(int red, int green, int blue, int alpha)
	{
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
	}

	public static int getColor(Vector4fc nrgba)
	{
		return ((((int) (nrgba.w() * 255)) & 0xFF) << 24) | ((((int) (nrgba.x() * 255)) & 0xFF) << 16) | ((((int) (nrgba.y() * 255)) & 0xFF) << 8) | (((int) (nrgba.z() * 255)) & 0xFF);
	}

	public static int getColor(int red, int green, int blue)
	{
		return ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
	}

	public static int getColor(Vector3fc nrgb)
	{
		return ((((int) (nrgb.x() * 255)) & 0xFF) << 16) | ((((int) (nrgb.y() * 255)) & 0xFF) << 8) | (((int) (nrgb.z() * 255)) & 0xFF);
	}

	public static Vector4i getRGBA(int color, Vector4i dst)
	{
		return dst.set((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
	}

	public static Vector4i getRGBA(Vector4fc nrgba, Vector4i dst)
	{
		return dst.set((int)(nrgba.x() * 255), (int)(nrgba.y() * 255), (int)(nrgba.z() * 255), (int)(nrgba.w() * 255));
	}

	public static Vector3i getRGB(int color, Vector3i dst)
	{
		return dst.set((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
	}

	public static Vector3i getRGB(Vector3fc nrgb, Vector3i dst)
	{
		return dst.set((int)(nrgb.x() * 255), (int)(nrgb.y() * 255), (int)(nrgb.z() * 255));
	}

	public static int getRed(int color)
	{
		return (color >> 16) & 0xFF;
	}

	public static int getGreen(int color)
	{
		return (color >> 8) & 0xFF;
	}

	public static int getBlue(int color)
	{
		return (color & 0xFF);
	}

	public static int getAlpha(int color)
	{
		return (color >> 24) & 0xFF;
	}

	public static Vector4f getNormalizedRGBA(int color, Vector4f dst)
	{
		return dst.set(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F, ((color >> 24) & 0xFF) / 255F);
	}

	public static Vector4f getNormalizedRGBA(int red, int green, int blue, int alpha, Vector4f dst)
	{
		return dst.set(red / 255F, green / 255F, blue / 255F, alpha / 255F);
	}

	public static Vector3f getNormalizedRGB(int color, Vector3f dst)
	{
		return dst.set(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F);
	}

	public static Vector3f getNormalizedRGB(int red, int green, int blue, Vector3f dst)
	{
		return dst.set(red / 255F, green / 255F, blue / 255F);
	}

	public static int HSBtoColor(float hue, float saturation, float brightness)
	{
		int r = 0, g = 0, b = 0;
		if (saturation == 0)
		{
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		}
		else
		{
			float h = (hue - (float)Math.floor(hue)) * 6.0f;
			float f = h - (float)java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h)
			{
				case 0:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}

		return (r << 16) | (g << 8) | (b << 0);
	}

	public static Vector3f HSBtoRGB(float hue, float saturation, float brightness, Vector3f dst)
	{
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float)Math.floor(hue)) * 6.0f;
			float f = h - (float)java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
				case 0:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}
		return dst.set(r, g, b);
	}

	public static Vector3f RGBtoHSB(int red, int green, int blue, Vector3f dst)
	{
		float hue, saturation, brightness;
		int cmax = (red > green) ? red : green;
		if (blue > cmax) cmax = blue;
		int cmin = (red < green) ? red : green;
		if (blue < cmin) cmin = blue;

		brightness = ((float) cmax) / 255.0f;
		if (cmax != 0)
		{
			saturation = ((float) (cmax - cmin)) / ((float) cmax);
		}
		else
		{
			saturation = 0;
		}

		if (saturation == 0)
		{
			hue = 0;
		}
		else
		{
			float redc = ((float) (cmax - red)) / ((float) (cmax - cmin));
			float greenc = ((float) (cmax - green)) / ((float) (cmax - cmin));
			float bluec = ((float) (cmax - blue)) / ((float) (cmax - cmin));
			if (red == cmax)
			{
				hue = bluec - greenc;
			}
			else if (green == cmax)
			{
				hue = 2.0f + redc - bluec;
			}
			else
			{
				hue = 4.0f + greenc - redc;
			}

			hue = hue / 6.0f;
			if (hue < 0)
			{
				hue = hue + 1.0f;
			}
		}
		return dst.set(hue, saturation, brightness);
	}

	private static final float GOLDEN_RATIO_CONJ = 0.618033988749895F;

	public static int getRandomColorWithGoldenRatio(Random random, float saturation, float brightness)
	{
		float hue = random.nextFloat();
		hue += GOLDEN_RATIO_CONJ;
		hue %= 1;
		return HSBtoColor(hue, saturation, brightness);
	}

	public static int getColorWithBrightness(int color, float brightness)
	{
		return getColor((int)(getRed(color) * brightness), (int)(getGreen(color) * brightness), (int)(getBlue(color) * brightness));
	}

	public static int getColorMix(int color, int other, float ratio)
	{
		if (ratio > 1) ratio = 1;
		else if (ratio < 0) ratio = 0;
		float invratio = 1 - ratio;

		int a1 = (color >> 24 & 0xff);
		int r1 = ((color & 0xff0000) >> 16);
		int g1 = ((color & 0xff00) >> 8);
		int b1 = (color & 0xff);

		int a2 = (other >> 24 & 0xff);
		int r2 = ((other & 0xff0000) >> 16);
		int g2 = ((other & 0xff00) >> 8);
		int b2 = (other & 0xff);

		int a = (int)((a1 * invratio) + (a2 * ratio));
		int r = (int)((r1 * invratio) + (r2 * ratio));
		int g = (int)((g1 * invratio) + (g2 * ratio));
		int b = (int)((b1 * invratio) + (b2 * ratio));

		return a << 24 | r << 16 | g << 8 | b;
	}
}
