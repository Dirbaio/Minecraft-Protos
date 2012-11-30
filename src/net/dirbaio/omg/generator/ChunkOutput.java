package net.dirbaio.omg.generator;

import net.dirbaio.omg.Chunk;

public interface ChunkOutput
{
    public void chunkDone(Chunk c);
    public void generationStarted(int xMin, int zMin, int xSize, int zSize);
    public void generationFinished();
}
