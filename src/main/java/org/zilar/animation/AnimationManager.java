package org.zilar.animation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/6/17.
 */
public class AnimationManager
{
	public static abstract class Animator<T>
	{
		private boolean paused = false;
		private boolean active = true;

		private float progress = 0;

		protected float nextFrameTime = 1;

		protected T source;

		public Animator(T src)
		{
			this.source = src;
		}

		public final void update(float delta)
		{
			this.progress += delta;
			if (this.progress >= this.nextFrameTime)
			{
				this.nextFrame();
				this.resetProgress();
			}
		}

		protected abstract void nextFrame();

		protected abstract void setFrame(int frameIndex);

		protected final void resetProgress()
		{
			this.progress = 0;
		}

		protected final void setNextFrameTime(float frameTime)
		{
			this.nextFrameTime = frameTime;
		}

		protected final void stop()
		{
			this.active = false;
		}

		public final void setPaused(boolean paused)
		{
			this.paused = paused;
		}

		public final boolean isPaused()
		{
			return this.paused;
		}

		public final boolean isActive()
		{
			return this.active;
		}

		public final T getSource()
		{
			return this.source;
		}
	}

	private final Set<Animator> animators = new HashSet<>();

	public void start(Animator animator)
	{
		this.animators.add(animator);
	}

	public void update(double delta)
	{
		Iterator<Animator> iter = this.animators.iterator();
		while(iter.hasNext())
		{
			Animator animator = iter.next();
			if (!animator.isActive())
			{
				iter.remove();
			}
			else if (!animator.isPaused())
			{
				animator.update((float) delta);
			}
		}
	}
}
