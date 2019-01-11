package canary.test.tilemapper.suger.dungeon;

/**
 * Created by Andy on 11/22/17.
 */
public class DungeonCamera
{
	private int x;
	private int y;
	private int width;
	private int height;

	public void move(int dx, int dy)
	{
		this.x += dx;
		this.y += dy;
	}

	public void resize(int dw, int dh)
	{
		this.width -= dw;
		this.height -= dh;

		if (this.width < 1) this.width = 1;
		if (this.height < 1) this.height = 1;
	}

	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
