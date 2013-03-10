package net.dirbaio.protos.generator;

import net.dirbaio.protos.Chunk;

public interface ChunkOutput
{
    public void chunkDone(Chunk c);
    public void generationStarted(int xMin, int zMin, int xSize, int zSize);
    public void generationFinished();
}
