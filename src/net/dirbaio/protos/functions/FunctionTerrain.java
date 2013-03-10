package net.dirbaio.protos.functions;

public abstract class FunctionTerrain extends Function
{

    public abstract short[] getBlockData(int px, int pz, int sx, int sz);
}
