package org.zilar.render.material;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class Property<C extends Context>
{
	protected C context;

	protected Property(C context)
	{
		this.context = context;
	}

	protected abstract void addSupportForMaterial(Material material);

	public final C bind(Material material)
	{
		this.context.material = material;
		return this.context;
	}
}
