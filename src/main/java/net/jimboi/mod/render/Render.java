package net.jimboi.mod.render;

import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.Iterator;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class Render
{
	protected static final Matrix4f _MAT_A = new Matrix4f();
	protected static final Matrix4f _MAT_B = new Matrix4f();
	protected static final Matrix4f _MAT_C = new Matrix4f();

	protected final Program program;

	public Render(Program program)
	{
		this.program = program;
	}

	public final void onRender(Instance inst)
	{
		Model model = inst.getModel();
		Material material = inst.getMaterial();

		this.preRender(this.program, model, material);
		this.doRender(this.program, model, material, inst);
		this.postRender(this.program, model, material);
	}

	public final void onRender(Iterator<Instance> iter)
	{
		Model model = null;
		Material material = null;
		boolean dirty = false;

		while(iter.hasNext())
		{
			Instance inst = iter.next();
			if (model != inst.getModel() || material != inst.getMaterial())
			{
				if (dirty)
				{
					this.postRender(this.program, model, material);
				}

				model = inst.getModel();
				material = inst.getMaterial();

				this.preRender(this.program, model, material);
				dirty = true;
			}

			this.doRender(this.program, model, material, inst);
		}

		if (dirty)
		{
			this.postRender(this.program, model, material);
		}
	}

	public abstract void preRender(Program program, Model model, Material material);
	public abstract void doRender(Program program, Model model, Material material, Instance inst);
	public abstract void postRender(Program program, Model model, Material material);

	public final Program getProgram()
	{
		return this.program;
	}
}
