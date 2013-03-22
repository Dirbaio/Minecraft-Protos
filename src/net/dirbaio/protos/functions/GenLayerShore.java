package net.dirbaio.protos.functions;

public class GenLayerShore extends BiomeFunction
{
    public BiomeFunction base;

    public GenLayerShore(BiomeFunction base)
    {
        this.base = base;
    }

    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] var5 = this.base.getBiomeData(px - 1, pz - 1, sx + 2, sz + 2);
        int[] var6 = IntCache.getIntCache(sx * sz);

        for (int var7 = 0; var7 < sz; ++var7)
        {
            for (int var8 = 0; var8 < sx; ++var8)
            {
                int var9 = var5[var8 + 1 + (var7 + 1) * (sx + 2)];
                int var10;
                int var11;
                int var12;
                int var13;

                if (var9 == Biome.mushroomIsland.biomeID)
                {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sx + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sx + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sx + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sx + 2)];

                    if (var10 != Biome.ocean.biomeID && var11 != Biome.ocean.biomeID && var12 != Biome.ocean.biomeID && var13 != Biome.ocean.biomeID)
                    {
                        var6[var8 + var7 * sx] = var9;
                    }
                    else
                    {
                        var6[var8 + var7 * sx] = Biome.mushroomIslandShore.biomeID;
                    }
                }
                else if (var9 != Biome.ocean.biomeID && var9 != Biome.river.biomeID && var9 != Biome.swampland.biomeID && var9 != Biome.extremeHills.biomeID)
                {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sx + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sx + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sx + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sx + 2)];

                    if (var10 != Biome.ocean.biomeID && var11 != Biome.ocean.biomeID && var12 != Biome.ocean.biomeID && var13 != Biome.ocean.biomeID)
                    {
                        var6[var8 + var7 * sx] = var9;
                    }
                    else
                    {
                        var6[var8 + var7 * sx] = Biome.beach.biomeID;
                    }
                }
                else if (var9 == Biome.extremeHills.biomeID)
                {
                    var10 = var5[var8 + 1 + (var7 + 1 - 1) * (sx + 2)];
                    var11 = var5[var8 + 1 + 1 + (var7 + 1) * (sx + 2)];
                    var12 = var5[var8 + 1 - 1 + (var7 + 1) * (sx + 2)];
                    var13 = var5[var8 + 1 + (var7 + 1 + 1) * (sx + 2)];

                    if (var10 == Biome.extremeHills.biomeID && var11 == Biome.extremeHills.biomeID && var12 == Biome.extremeHills.biomeID && var13 == Biome.extremeHills.biomeID)
                    {
                        var6[var8 + var7 * sx] = var9;
                    }
                    else
                    {
                        var6[var8 + var7 * sx] = Biome.extremeHillsEdge.biomeID;
                    }
                }
                else
                {
                    var6[var8 + var7 * sx] = var9;
                }
            }
        }

        return var6;
    }
}
