package org.zilar.render.material;

/**
 * Created by Andy on 8/12/17.
 */
public class Context
{
	protected Material material;

	protected Context(){}

	protected void setMaterial(Material material)
	{
		this.material = material;
	}

	public final Material unbind()
	{
		Material result = this.material;
		this.material = null;
		return result;
	}
}
