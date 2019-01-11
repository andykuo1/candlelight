package boron.bstone.living;

/**
 * Created by Andy on 8/12/17.
 */
public interface ILiving
{
	void onLivingCreate(LivingManager livingManager);
	void onLivingUpdate();
	void onLivingLateUpdate();
	void onLivingDestroy();

	void setDead();
	boolean isDead();

	void setLivingManager(LivingManager livingManager);
	void setLivingID(int id);

	LivingManager getLivingManager();
	int getLivingID();
}
