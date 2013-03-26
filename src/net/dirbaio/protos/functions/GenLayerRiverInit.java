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
        int[] var6 = ArrayCache.newInt(sx * sz);

        for (int var7 = 0; var7 < sz; ++var7)
            for (int var8 = 0; var8 < sx; ++var8)
                var6[var8 + var7 * sx] = var5[var8 + var7 * sx] > 0 ? this.randForPos(2, var8 + px, var7 + pz, 0) + 2 : 0;

        return var6;
    }
}
