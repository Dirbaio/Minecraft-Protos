package net.dirbaio.protos.functions;

import java.util.Random;

public abstract class BiomeFunction extends Function
{
    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;

    protected int randForPos(int max, long px, long pz, long n)
    {
        long seed = px*341651197+pz*84719323+n*517375 + randomSeedOffset;
        seed = (seed ^ multiplier) & mask;
        seed = (seed * multiplier + addend) & mask;
        if ((max & -max) == max)  // i.e., max is a power of 2
        {
            seed = seed >>> (48-31);
            return (int)((max * seed) >> 31);
        }
        
        return (int)(((seed % max)+max)%max);
    }
    
    public abstract int[] getBiomeData(int px, int pz, int sx, int sz);
}
