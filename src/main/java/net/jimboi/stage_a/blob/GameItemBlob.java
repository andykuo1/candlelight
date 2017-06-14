package net.jimboi.stage_a.blob;

import net.jimboi.stage_a.mod.ModMaterial;
import net.jimboi.stage_a.mod.instance.Instance;
import net.jimboi.stage_a.mod.model.Model;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.qsilver.living.Living;

/**
 * Created by Andy on 4/19/17.
 */
public class GameItemBlob extends Living
{
	private final Transform3 transform = Transform3.create();

	private Instance instance;

	public GameItemBlob(Model model, ModMaterial material, String renderType)
	{
		this.instance = new Instance(model, material, renderType);
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	@Override
	public void onLateUpdate()
	{
		super.onLateUpdate();

		this.instance.setTransformation(this.transform);
	}

	public Instance getInstance()
	{
		return this.instance;
	}

	public Transform3 getTransform()
	{
		return this.transform;
	}
}
