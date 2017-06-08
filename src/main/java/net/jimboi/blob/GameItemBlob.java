package net.jimboi.blob;

import net.jimboi.mod.instance.Instance;
import net.jimboi.mod2.transform.Transform3;

import org.qsilver.living.Living;
import org.qsilver.material.Material;
import org.qsilver.model.Model;

/**
 * Created by Andy on 4/19/17.
 */
public class GameItemBlob extends Living
{
	private final Transform3 transform = Transform3.create();

	private Instance instance;

	public GameItemBlob(Model model, Material material, String renderType)
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
