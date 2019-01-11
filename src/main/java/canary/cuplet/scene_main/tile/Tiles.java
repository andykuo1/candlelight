package canary.cuplet.scene_main.tile;

/**
 * Created by Andy on 9/1/17.
 */
public class Tiles
{
	public static final Tile air = Tile.register(0, new TileAir());
	public static final Tile dirt = Tile.register(1, new TileDirt());
	public static final Tile stone = Tile.register(2, new TileStone());
}
