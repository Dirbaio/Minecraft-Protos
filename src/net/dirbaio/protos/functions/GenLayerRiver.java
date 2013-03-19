package net.dirbaio.protos.functions;

public class GenLayerRiver extends BiomeFunction
{
    BiomeFunction base;

    public GenLayerRiver(BiomeFunction base)
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
                int var13 = var9[var12 + 0 + (var11 + 1) * var7];
                int var14 = var9[var12 + 2 + (var11 + 1) * var7];
                int var15 = var9[var12 + 1 + (var11 + 0) * var7];
                int var16 = var9[var12 + 1 + (var11 + 2) * var7];
                int var17 = var9[var12 + 1 + (var11 + 1) * var7];

                if (var17 != 0 && var13 != 0 && var14 != 0 && var15 != 0 && var16 != 0 && var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16)
                {
                    var10[var12 + var11 * sx] = -1;
                }
                else
                {
                    var10[var12 + var11 * sx] = Biome.river.biomeID;
                }
            }
        }

        return var10;
    }
}
