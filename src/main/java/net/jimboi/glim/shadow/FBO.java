package net.jimboi.glim.shadow;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.bstone.window.ViewPort;
import org.bstone.window.Window;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 6/7/17.
 */
public final class FBO implements AutoCloseable
{
	public static final Set<FBO> FBOS = new HashSet<>();

	private final ViewPort viewport;

	private int handle;
	private Texture texture;

	public FBO(int width, int height, boolean colorBuffer)
	{
		this.viewport = new ViewPort(width, height);

		this.handle = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.handle);

		if (!colorBuffer)
		{
			GL11.glDrawBuffer(GL11.GL_NONE);
			GL11.glReadBuffer(GL11.GL_NONE);
		}

		this.texture = new Texture(this.viewport.getWidth(), this.viewport.getHeight(), GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE, Bitmap.Format.DEPTH);

		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture.handle(), 0);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		FBOS.add(this);
	}

	@Override
	public void close()
	{
		this.unbind(null);

		GL30.glDeleteFramebuffers(this.handle);
		this.texture.close();
		this.handle = 0;

		FBOS.remove(this);
	}

	public int handle()
	{
		return this.handle;
	}

	public void bind(Window window)
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, this.handle);

		if (window != null)
		{
			window.setCurrentViewPort(this.viewport);
		}
	}

	public void unbind(Window window)
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

		if (window != null)
		{
			window.removeViewPort(this.viewport);
		}
	}

	public ViewPort getViewPort()
	{
		return this.viewport;
	}

	public Texture getTexture()
	{
		return this.texture;
	}
}
