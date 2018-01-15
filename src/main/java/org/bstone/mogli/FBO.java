package org.bstone.mogli;

import org.bstone.RefCountSet;
import org.bstone.window.Window;
import org.bstone.window.view.ViewPort;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.util.Set;

/**
 * Created by Andy on 6/7/17.
 */
public final class FBO implements AutoCloseable
{
	public static final Set<FBO> FBOS = new RefCountSet<>("FBO");

	private final ViewPort viewport;

	private int handle;
	private Texture texture;

	public FBO(int width, int height, boolean colorBuffer)
	{
		this.viewport = new ViewPort(0, 0, width, height);

		this.handle = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.handle);

		if (!colorBuffer)
		{
			GL11.glDrawBuffer(GL11.GL_NONE);
			GL11.glReadBuffer(GL11.GL_NONE);
		}

		//this.texture = new Texture(this.viewport.getWidth(), this.VIEW.getHeight(), GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE, Bitmap.Format.DEPTH);

		//GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture.handle(), 0);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

		FBOS.add(this);
	}

	@Override
	public void close() throws Exception
	{
		GL30.glDeleteFramebuffers(this.handle);
		this.handle = 0;

		FBOS.remove(this);
	}

	public FBO attachTexture(Texture texture, int attachment, boolean updateViewPort)
	{
		this.bind();
		this.texture = texture;
		this.viewport.setWidth((int) this.texture.width());
		this.viewport.setHeight((int) this.texture.height());
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, attachment, this.texture.handle(), 0);
		this.unbind();
		return this;
	}

	public int handle()
	{
		return this.handle;
	}

	public void bind()
	{
		this.bind(null, true, true);
	}

	public void unbind()
	{
		this.unbind(null, true, true);
	}

	public void bind(Window window, boolean draw, boolean read)
	{
		if (draw && read)
		{
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.handle);
		}
		else if (draw)
		{
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, this.handle);

			if (window != null)
			{
				window.getView().setCurrentViewPort(this.viewport);
			}
		}
		else
		{
			GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.handle);
		}
	}

	public void unbind(Window window, boolean draw, boolean read)
	{
		if (draw && read)
		{
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		}
		else if (draw)
		{
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

			if (window != null)
			{
				window.getView().removeViewPort(this.viewport);
			}
		}
		else
		{
			GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
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

	public boolean hasTexture()
	{
		return this.texture != null;
	}
}
