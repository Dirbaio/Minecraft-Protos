package net.dirbaio.protos.functions;

import java.util.Random;

public abstract class BiomeFunction extends Function
{
    Random r = new Random();
    protected int nextInt(int max)
    {
        //TODO FIX
        return r.nextInt(max);
        
        /*
        int var2 = (int)((this.chunkSeed >> 24) % (long)par1);

        if (var2 < 0)
        {
            var2 += par1;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return var2;
        */
    }
    
    protected void setPosForRandom(int px, int pz)
    {
        
    }

    public abstract int[] getBiomeData(int px, int pz, int sx, int sz);
}
