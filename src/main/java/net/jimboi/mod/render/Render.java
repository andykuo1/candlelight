package net.jimboi.mod.render;

import net.jimboi.mod.Material;
import net.jimboi.mod.instance.Instance;
import net.jimboi.mod2.material.property.PropertyDiffuse;
import net.jimboi.mod2.material.property.PropertySpecular;
import net.jimboi.mod2.material.property.PropertyTexture;

import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.qsilver.model.Model;

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

	private Material material = new Material();

	public final void onRender(Instance inst)
	{
		Model model = inst.getModel();
		org.qsilver.material.Material material = inst.getMaterial();
		if (material.hasComponent(PropertyDiffuse.class))
		{
			PropertyDiffuse prop = material.getComponent(PropertyDiffuse.class);
			this.material.mainColor.set(prop.diffuseColor);
		}
		if (material.hasComponent(PropertySpecular.class))
		{
			PropertySpecular prop = material.getComponent(PropertySpecular.class);
			this.material.specularColor.set(prop.specularColor);
			this.material.shininess = prop.shininess;
		}
		if (material.hasComponent(PropertyTexture.class))
		{
			PropertyTexture prop = material.getComponent(PropertyTexture.class);
			this.material.texture = prop.texture;
			this.material.sprite = prop.sprite;
		}

		this.preRender(this.program, model, this.material);
		this.doRender(this.program, model, this.material, inst);
		this.postRender(this.program, model, this.material);
	}

	public abstract void preRender(Program program, Model model, Material material);
	public abstract void doRender(Program program, Model model, Material material, Instance inst);
	public abstract void postRender(Program program, Model model, Material material);

	public final Program getProgram()
	{
		return this.program;
	}
}
