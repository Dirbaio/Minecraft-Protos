package net.dirbaio.omg.generator;

import net.dirbaio.omg.Chunk;

public interface ChunkOutput
{
    public void chunkDone(Chunk c);
    public void generationFinished();
}
