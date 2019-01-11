package boron.base.sprite;

/**
 * Created by Andy on 5/10/17.
 */
public class TextureAtlas
{
	private final Sprite[] sprites;

	public TextureAtlas(Sprite... sprites)
	{
		this.sprites = sprites;
	}

	public Sprite getSprite(int index)
	{
		return this.sprites[index];
	}

	public int length()
	{
		return this.sprites.length;
	}
}
