package net.dirbaio.protos.functions;

public class GenLayerHills extends BiomeFunction
{
    public BiomeFunction base;
    public GenLayerHills(BiomeFunction base)
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

                if (this.randForPos(3, var8 + px, var7 + pz, 0) == 0)
                {
                    int var10 = var9;

                    if (var9 == Biome.desert.biomeID)
                    {
                        var10 = Biome.desertHills.biomeID;
                    }
                    else if (var9 == Biome.forest.biomeID)
                    {
                        var10 = Biome.forestHills.biomeID;
                    }
                    else if (var9 == Biome.taiga.biomeID)
                    {
                        var10 = Biome.taigaHills.biomeID;
                    }
                    else if (var9 == Biome.plains.biomeID)
                    {
                        var10 = Biome.forest.biomeID;
                    }
                    else if (var9 == Biome.icePlains.biomeID)
                    {
                        var10 = Biome.iceMountains.biomeID;
                    }
                    else if (var9 == Biome.jungle.biomeID)
                    {
                        var10 = Biome.jungleHills.biomeID;
                    }

                    if (var10 == var9)
                    {
                        var6[var8 + var7 * sx] = var9;
                    }
                    else
                    {
                        int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (sx + 2)];
                        int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (sx + 2)];
                        int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (sx + 2)];
                        int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (sx + 2)];

                        if (var11 == var9 && var12 == var9 && var13 == var9 && var14 == var9)
                        {
                            var6[var8 + var7 * sx] = var10;
                        }
                        else
                        {
                            var6[var8 + var7 * sx] = var9;
                        }
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
