package canary.bstone.util.chunk;

import org.joml.Vector2i;
import org.joml.Vector2ic;

/**
 * Created by Andy on 11/13/17.
 */
public interface ChunkLoader<T extends Chunk>
{
	T onChunkLoad(ChunkMap<T> chunkMap, Vector2i chunkCoord, int chunkSize);

	void onChunkUnload(ChunkMap<T> chunkMap, Vector2ic chunkCoord, T chunk);

	void onChunkUpdate(ChunkMap<T> chunkMap, T chunk);
}
