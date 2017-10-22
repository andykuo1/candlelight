package net.jimboi.canary.stage_a.owle;

/**
 * Created by Andy on 9/13/17.
 */
public class ViewComponentText extends ViewComponent
{
	private String text;

	private int color = 0xFFFFFF;
	private int maxLength = -1;
	private int maxLines = -1;

	public ViewComponentText(int x, int y, String text)
	{
		super(x, y);

		this.text = text;
	}

	@Override
	public void render(RasterView view)
	{
		if (this.maxLines == 0 || this.maxLength == 0) return;

		int i = -1;
		int j = 0;
		for(int k = 0; k < this.text.length(); ++k)
		{
			char c = this.text.charAt(k);
			if (c == '\n')
			{
				i = -1;
				++j;
				if (this.maxLines > 0 && j >= this.maxLines) return;
				continue;
			}
			++i;

			if (i >= this.maxLength) continue;
			view.setPixel(this.x + i, this.y + j, this.color, (byte) this.text.charAt(k));
		}
	}

	public ViewComponentText setMaxLength(int length)
	{
		this.maxLength = length;
		return this;
	}

	public ViewComponentText setMaxLines(int lines)
	{
		this.maxLines = lines;
		return this;
	}

	public ViewComponentText setColor(int color)
	{
		this.color = color;
		return this;
	}

	public ViewComponentText setText(String text)
	{
		this.text = text;
		return this;
	}

	public void append(String text)
	{
		this.text += text;
	}

	public int getColor()
	{
		return this.color;
	}

	public String getText()
	{
		return this.text;
	}
}
