package net.jimboi.apricot.stage_a.mod.render;

import net.jimboi.apricot.stage_a.mod.ModMaterial;
import net.jimboi.apricot.stage_a.mod.instance.Instance;
import net.jimboi.apricot.stage_a.mod.model.Model;

import org.bstone.mogli.Program;
import org.joml.Matrix4f;

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
		ModMaterial material = inst.getMaterial();

		this.preRender(this.program, model, material);
		this.doRender(this.program, model, material, inst);
		this.postRender(this.program, model, material);
	}

	public abstract void preRender(Program program, Model model, ModMaterial material);

	public abstract void doRender(Program program, Model model, ModMaterial material, Instance inst);

	public abstract void postRender(Program program, Model model, ModMaterial material);

	public final Program getProgram()
	{
		return this.program;
	}
}
