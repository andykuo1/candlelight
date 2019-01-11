package canary.test.sleuth.data;

import static canary.test.sleuth.data.BodyOrgan.BRAIN;
import static canary.test.sleuth.data.BodyOrgan.EARS;
import static canary.test.sleuth.data.BodyOrgan.EYES;
import static canary.test.sleuth.data.BodyOrgan.GENITAL;
import static canary.test.sleuth.data.BodyOrgan.HEART;
import static canary.test.sleuth.data.BodyOrgan.LUNGS;
import static canary.test.sleuth.data.BodyOrgan.SPINE;
import static canary.test.sleuth.data.BodyOrgan.STOMACH;
import static canary.test.sleuth.data.BodyOrgan.THROAT;
import static canary.test.sleuth.data.BodyOrgan.TONGUE;

/**
 * Created by Andy on 9/23/17.
 */
public enum BodyPart
{
	NONE,
	SKULL(BRAIN),
	RIGHT_EAR(EARS),
	LEFT_EAR(EARS),
	RIGHT_JAW,
	LEFT_JAW,
	RIGHT_EYE(EYES),
	LEFT_EYE(EYES),
	NOSE,
	RIGHT_CHEEK,
	LEFT_CHEEK,
	MOUTH(TONGUE),
	CHIN,
	NECK_THROAT(THROAT),
	COLLAR,
	RIGHT_SHOULDER,
	LEFT_SHOULDER,
	STERNUM(HEART),
	CHEST(HEART),
	RIGHT_RIBS(LUNGS),
	LEFT_RIBS(LUNGS),
	RIGHT_WAIST,
	LEFT_WAIST,
	ABDOMEN(STOMACH),
	PELVIS(GENITAL),
	UPPER_BACK(HEART),
	LOWER_BACK(SPINE),
	RIGHT_ARM,
	RIGHT_FOREARM,
	RIGHT_HAND,
	LEFT_ARM,
	LEFT_FOREARM,
	LEFT_HAND,
	RIGHT_WRIST,
	RIGHT_PALM,
	RIGHT_FINGERS,
	LEFT_WRIST,
	LEFT_PALM,
	LEFT_FINGERS,
	RIGHT_THIGH,
	RIGHT_LEG,
	RIGHT_FOOT,
	LEFT_THIGH,
	LEFT_LEG,
	LEFT_FOOT,
	RIGHT_ANKLE,
	RIGHT_SOLE,
	RIGHT_TOES,
	LEFT_ANKLE,
	LEFT_SOLE,
	LEFT_TOES,
	;

	private final BodyOrgan organ;

	BodyPart()
	{
		this.organ = null;
	}

	BodyPart(BodyOrgan organ)
	{
		this.organ = organ;
	}

	public boolean hasOrgan()
	{
		return this.organ != null;
	}

	public BodyOrgan getOrgan()
	{
		return this.organ;
	}
}
