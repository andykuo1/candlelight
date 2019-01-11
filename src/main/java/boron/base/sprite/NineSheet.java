package boron.base.sprite;

import boron.base.asset.Asset;

import boron.bstone.util.Direction;

/**
 * Created by Andy on 7/12/17.
 */
public final class NineSheet
{
	private Asset<TextureAtlas> textureAtlas;
	private final int[] indices;

	public NineSheet(Asset<TextureAtlas> textureAtlas)
	{
		this(textureAtlas, 0, 1, 2, 3, 4, 5, 6, 7, 8);
	}

	public NineSheet(Asset<TextureAtlas> textureAtlas, int topLeft, int top, int topRight, int bodyLeft, int body, int bodyRight, int botLeft, int bot, int botRight)
	{
		this.textureAtlas = textureAtlas;
		this.indices = new int[] {
				topLeft, top, topRight,
				bodyLeft, body, bodyRight,
				botLeft, bot, botRight
		};
	}

	public Sprite get(Direction direction)
	{
		switch (direction)
		{
			case EAST:
				return this.textureAtlas.getSource().getSprite(this.indices[5]);
			case NORTHEAST:
				return this.textureAtlas.getSource().getSprite(this.indices[2]);
			case NORTH:
				return this.textureAtlas.getSource().getSprite(this.indices[1]);
			case NORTHWEST:
				return this.textureAtlas.getSource().getSprite(this.indices[0]);
			case WEST:
				return this.textureAtlas.getSource().getSprite(this.indices[3]);
			case SOUTHWEST:
				return this.textureAtlas.getSource().getSprite(this.indices[6]);
			case SOUTH:
				return this.textureAtlas.getSource().getSprite(this.indices[7]);
			case SOUTHEAST:
				return this.textureAtlas.getSource().getSprite(this.indices[8]);
			case CENTER:
			default:
				return this.textureAtlas.getSource().getSprite(this.indices[4]);
		}
	}
}
