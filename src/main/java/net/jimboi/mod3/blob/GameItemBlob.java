package net.jimboi.mod3.blob;

import net.jimboi.mod.transform.Transform3;

import org.qsilver.living.Living;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

/**
 * Created by Andy on 4/19/17.
 */
public class GameItemBlob extends Living
{
	private final Transform3 transform = Transform3.create();

	private Instance instance;

	public GameItemBlob(Model model, Material material)
	{
		this.instance = new Instance(model, material);
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
