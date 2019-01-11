package apricot.stage_a.blob;

import apricot.base.living.OldLiving;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.model.Model;

import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 4/19/17.
 */
public class GameItemBlob extends OldLiving
{
	private final Transform3 transform = new Transform3();

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
