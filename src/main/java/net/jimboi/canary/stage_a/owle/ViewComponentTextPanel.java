package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

/**
 * Created by Andy on 9/14/17.
 */
public class ViewComponentTextPanel extends ViewComponentPanel
{
	private final StringBuffer buffer;

	public ViewComponentTextPanel(int x, int y, int width, int height)
	{
		super(x, y, width, height);

		this.buffer = new StringBuffer();
	}

	@Override
	public void render(RasterizedView view)
	{
		super.render(view);

		int x = -1;
		int y = -1;
		for(int i = 0; i < this.buffer.length(); ++i)
		{
			char c = this.buffer.charAt(i);
			if (c == '\n')
			{
				++y;
				if (y > this.height - 3) return;
				x = -1;
				continue;
			}
			++x;
			if (x > this.width - 3) continue;

			view.glyph(1 + (this.x + x), this.height - (this.y + y), (char) c);
		}
	}

	public void append(String text)
	{
		this.buffer.append(text);
	}

	public void clear()
	{
		this.buffer.setLength(0);
	}
}
