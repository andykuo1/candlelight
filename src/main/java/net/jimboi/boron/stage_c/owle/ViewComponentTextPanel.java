package net.jimboi.boron.stage_c.owle;

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
	public void render(RasterView view)
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

			view.setPixelType(1 + (this.x + x), this.height - (this.y + y), (byte) c);
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
