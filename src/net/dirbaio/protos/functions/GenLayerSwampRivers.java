package net.dirbaio.protos.functions;

public class GenLayerSwampRivers extends BiomeFunction
{
    public BiomeFunction base;

    public GenLayerSwampRivers(BiomeFunction base)
    {
        this.base = base;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] var5 = this.base.getBiomeData(px - 1, pz - 1, sx + 2, sz + 2);
        int[] var6 = ArrayCache.newInt(sx * sz);

        for (int var7 = 0; var7 < sz; ++var7)
        {
            for (int var8 = 0; var8 < sx; ++var8)
            {
                int var9 = var5[var8 + 1 + (var7 + 1) * (sx + 2)];

                if ((var9 != Biome.swampland.biomeID || this.randForPos(6, var8 + px, var7 + pz, 0) != 0) 
                    && (var9 != Biome.jungle.biomeID && var9 != Biome.jungleHills.biomeID 
                        || this.randForPos(8, var8 + px, var7 + pz, 1) != 0))
                {
                    var6[var8 + var7 * sx] = var9;
                }
                else
                {
                    var6[var8 + var7 * sx] = Biome.river.biomeID;
                }
            }
        }

        return var6;
    }
}
