package net.dirbaio.protos.functions;

public class GenLayerRiverInit extends BiomeFunction
{
    public BiomeFunction base;

    public GenLayerRiverInit(BiomeFunction base)
    {
        this.base = base;
    }
    
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] var5 = this.base.getBiomeData(px, pz, sx, sz);
        int[] var6 = IntCache.getIntCache(sx * sz);

        for (int var7 = 0; var7 < sz; ++var7)
        {
            for (int var8 = 0; var8 < sx; ++var8)
            {
                this.setPosForRandom(var8 + px, var7 + pz);
                var6[var8 + var7 * sx] = var5[var8 + var7 * sx] > 0 ? this.nextInt(2) + 2 : 0;
            }
        }

        return var6;
    }
}
