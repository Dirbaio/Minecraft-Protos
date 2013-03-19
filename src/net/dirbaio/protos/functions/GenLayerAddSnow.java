package net.dirbaio.protos.functions;

public class GenLayerAddSnow extends BiomeFunction
{
    public BiomeFunction base;
    public GenLayerAddSnow(BiomeFunction base)
    {
        this.base = base;
    }

    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int var5 = px - 1;
        int var6 = pz - 1;
        int var7 = sx + 2;
        int var8 = sz + 2;
        int[] var9 = this.base.getBiomeData(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(sx * sz);

        for (int var11 = 0; var11 < sz; ++var11)
        {
            for (int var12 = 0; var12 < sx; ++var12)
            {
                int var13 = var9[var12 + 1 + (var11 + 1) * var7];
                this.setPosForRandom(var12 + px, var11 + pz);

                if (var13 == 0)
                {
                    var10[var12 + var11 * sx] = 0;
                }
                else
                {
                    int var14 = this.nextInt(5);

                    if (var14 == 0)
                    {
                        var14 = Biome.icePlains.biomeID;
                    }
                    else
                    {
                        var14 = 1;
                    }

                    var10[var12 + var11 * sx] = var14;
                }
            }
        }

        return var10;
    }
}
