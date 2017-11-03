package net.jimboi.apricot.stage_a.mod;

import org.bstone.util.ColorUtil;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by Andy on 4/9/17.
 */
public class ModLight
{
	public Vector4f position = new Vector4f();
	public Vector3f color = new Vector3f();
	public float attentuation = 0;
	public float ambientCoefficient = 0;

	public float coneAngle = 0;
	public Vector3f coneDirection = new Vector3f();

	public static ModLight createPointLight(float x, float y, float z, int color, float intensity, float attenuation, float ambientCoefficient)
	{
		ModLight light = new ModLight();
		light.position.set(x, y, z, 1);
		ColorUtil.getNormalizedRGB(color, light.color).mul(intensity);
		light.attentuation = attenuation;
		light.ambientCoefficient = ambientCoefficient;
		light.coneAngle = 360;
		light.coneDirection = new Vector3f(0, 0, 0);
		return light;
	}

	public static ModLight createSpotLight(float x, float y, float z, int color, float intensity, float attenuation, float ambientCoefficient, float coneAngle, float coneDirectionX, float coneDirectionY, float coneDirectionZ)
	{
		ModLight light = new ModLight();
		light.position.set(x, y, z, 1);
		ColorUtil.getNormalizedRGB(color, light.color).mul(intensity);
		light.attentuation = attenuation;
		light.ambientCoefficient = ambientCoefficient;
		light.coneAngle = coneAngle;
		light.coneDirection = new Vector3f(coneDirectionX, coneDirectionY, coneDirectionZ);
		return light;
	}

	public static ModLight createDirectionLight(float x, float y, float z, int color, float intensity, float ambientCoefficient)
	{
		ModLight light = new ModLight();
		light.position.set(x, y, z, 0);
		ColorUtil.getNormalizedRGB(color, light.color).mul(intensity);
		light.attentuation = 0;
		light.ambientCoefficient = ambientCoefficient;
		light.coneAngle = 0;
		light.coneDirection = new Vector3f(0, 0, 0);
		return light;
	}
}
