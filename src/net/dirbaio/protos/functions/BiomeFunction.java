package net.dirbaio.protos.functions;

import java.util.Random;

public abstract class BiomeFunction extends Function
{
    Random r = new Random();
    
    protected int randForPos(int max, int px, int pz, int n)
    {
        r.setSeed(px*341651197+pz*84719324+n + randomSeedOffset);
        return r.nextInt(max);
    }
    
    public abstract int[] getBiomeData(int px, int pz, int sx, int sz);
}
