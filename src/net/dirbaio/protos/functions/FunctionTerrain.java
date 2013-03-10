package net.dirbaio.protos.functions;

public abstract class FunctionTerrain extends Function
{

    public abstract short[] getTerrainData(int px, int pz, int sx, int sz);
}
