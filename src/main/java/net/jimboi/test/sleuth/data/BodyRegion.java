package net.jimboi.test.sleuth.data;

import org.bstone.util.direction.Direction;

import static net.jimboi.test.sleuth.data.BodyPart.ABDOMEN;
import static net.jimboi.test.sleuth.data.BodyPart.CHEST;
import static net.jimboi.test.sleuth.data.BodyPart.CHIN;
import static net.jimboi.test.sleuth.data.BodyPart.COLLAR;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_ANKLE;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_ARM;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_CHEEK;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_EAR;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_EYE;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_FINGERS;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_FOOT;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_FOREARM;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_HAND;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_JAW;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_LEG;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_PALM;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_RIBS;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_SHOULDER;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_SOLE;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_THIGH;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_TOES;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_WAIST;
import static net.jimboi.test.sleuth.data.BodyPart.LEFT_WRIST;
import static net.jimboi.test.sleuth.data.BodyPart.LOWER_BACK;
import static net.jimboi.test.sleuth.data.BodyPart.MOUTH;
import static net.jimboi.test.sleuth.data.BodyPart.NECK_THROAT;
import static net.jimboi.test.sleuth.data.BodyPart.NONE;
import static net.jimboi.test.sleuth.data.BodyPart.NOSE;
import static net.jimboi.test.sleuth.data.BodyPart.PELVIS;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_ANKLE;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_ARM;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_CHEEK;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_EAR;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_EYE;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_FINGERS;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_FOOT;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_FOREARM;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_HAND;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_JAW;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_LEG;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_PALM;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_RIBS;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_SHOULDER;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_SOLE;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_THIGH;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_TOES;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_WAIST;
import static net.jimboi.test.sleuth.data.BodyPart.RIGHT_WRIST;
import static net.jimboi.test.sleuth.data.BodyPart.SKULL;
import static net.jimboi.test.sleuth.data.BodyPart.STERNUM;
import static net.jimboi.test.sleuth.data.BodyPart.UPPER_BACK;

/**
 * Created by Andy on 9/23/17.
 */
public enum BodyRegion
{
	HEAD(
			SKULL, SKULL, SKULL,
			RIGHT_EAR, SKULL, LEFT_EAR,
			RIGHT_JAW, NONE, LEFT_JAW),
	FACE(
			RIGHT_EYE, NOSE, LEFT_EYE,
			RIGHT_CHEEK, MOUTH, LEFT_CHEEK,
			RIGHT_JAW, CHIN, LEFT_JAW),
	NECK(
			NONE, NECK_THROAT, NONE,
			COLLAR, NECK_THROAT, COLLAR,
			RIGHT_SHOULDER, NECK_THROAT, LEFT_SHOULDER),
	TORSO(
			RIGHT_SHOULDER, STERNUM, LEFT_SHOULDER,
			RIGHT_RIBS, CHEST, LEFT_RIBS,
			RIGHT_WAIST, ABDOMEN, LEFT_WAIST),
	BACK(
			RIGHT_SHOULDER, NECK_THROAT, LEFT_SHOULDER,
			RIGHT_RIBS, UPPER_BACK, LEFT_RIBS,
			RIGHT_WAIST, LOWER_BACK, LEFT_WAIST),
	WAIST(
			RIGHT_WAIST, ABDOMEN, LEFT_WAIST,
			RIGHT_WAIST, ABDOMEN, LEFT_WAIST,
			RIGHT_WAIST, PELVIS, LEFT_WAIST),
	ARM(
			RIGHT_ARM, NONE, LEFT_ARM,
			RIGHT_FOREARM, NONE, LEFT_FOREARM,
			RIGHT_HAND, NONE, LEFT_HAND),
	HAND(
			RIGHT_WRIST, NONE, LEFT_WRIST,
			RIGHT_PALM, NONE, LEFT_PALM,
			RIGHT_FINGERS, NONE, LEFT_FINGERS),
	LEG(
			RIGHT_THIGH, NONE, LEFT_THIGH,
			RIGHT_LEG, NONE, LEFT_LEG,
			RIGHT_FOOT, NONE, LEFT_FOOT),
	FOOT(
			RIGHT_ANKLE, NONE, LEFT_ANKLE,
			RIGHT_SOLE, NONE, LEFT_SOLE,
			RIGHT_TOES, NONE, LEFT_TOES);

	public static BodyRegion[] getNeighbors(BodyRegion region)
	{
		switch (region)
		{
			case HEAD: return new BodyRegion[] {FACE, NECK};
			case FACE: return new BodyRegion[] {HEAD, NECK};
			case NECK: return new BodyRegion[] {HEAD, FACE, TORSO, BACK};
			case TORSO: return new BodyRegion[] {NECK, WAIST};
			case BACK: return new BodyRegion[] {NECK, WAIST};
			case WAIST: return new BodyRegion[] {TORSO, LEG};
			case ARM: return new BodyRegion[] {TORSO, HAND};
			case HAND: return new BodyRegion[] {ARM};
			case LEG: return new BodyRegion[] {WAIST, FOOT};
			case FOOT: return new BodyRegion[] {LEG};
			default: return new BodyRegion[0];
		}
	}

	private final BodyPart[] parts = new BodyPart[9];

	BodyRegion(BodyPart northwest, BodyPart north, BodyPart northeast, BodyPart west, BodyPart center, BodyPart east, BodyPart southwest, BodyPart south, BodyPart southeast)
	{
		this.parts[0] = northwest;
		this.parts[1] = north;
		this.parts[2] = northeast;
		this.parts[3] = west;
		this.parts[4] = center;
		this.parts[5] = east;
		this.parts[6] = southwest;
		this.parts[7] = south;
		this.parts[8] = southeast;
	}

	public BodyPart getPart(Direction direction)
	{
		BodyPart part = this.parts[direction.ordinal()];
		if (part == NONE) throw new IllegalArgumentException("not a valid direction for region");
		return part;
	}
}
