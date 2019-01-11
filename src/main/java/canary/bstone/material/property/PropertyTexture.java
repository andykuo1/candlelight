package canary.bstone.material.property;

import canary.bstone.mogli.Program;
import canary.bstone.mogli.Texture;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyTexture extends PropertyAsset<Texture>
{
	private final int sampler;

	public PropertyTexture(String name, int sampler)
	{
		super("texture", name);

		if (sampler < 0 || sampler > 31) throw new IllegalArgumentException("texture sampler indices can only be from 0 to 31");

		this.sampler = sampler;
	}

	@Override
	public void apply(Program program, Texture value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform1i(location, this.sampler);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + this.sampler);
		value.bind();
	}

	public final int getSamplerIndex()
	{
		return this.sampler;
	}
}
