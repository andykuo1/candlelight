package org.zilar.animation;

import org.zilar.sprite.SpriteSheet;

/**
 * Created by Andy on 7/6/17.
 */
public class AnimatorSpriteSheet extends AnimationManager.Animator<SpriteSheet>
{
	public AnimatorSpriteSheet(SpriteSheet src, float frameTime)
	{
		super(src);

		this.setNextFrameTime(frameTime);
	}

	@Override
	protected void nextFrame()
	{
		this.source.next();
	}

	@Override
	protected void setFrame(int frameIndex)
	{
		this.source.setSpriteIndex(frameIndex);
		this.resetProgress();
	}
}
