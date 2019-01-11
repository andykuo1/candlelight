package apricot.stage_a.blob.livings;

/**
 * Created by Andy on 5/20/17.
 */
public class LivingZombie extends Living3
{
	public LivingZombie(float x, float y, float z)
	{
		super(x, y, z);
	}

	@Override
	public String getModelID()
	{
		return "plane";
	}

	@Override
	public String getMaterialID()
	{
		return "billboard";
	}

	@Override
	public String getRenderType()
	{
		return "billboard";
	}
}
