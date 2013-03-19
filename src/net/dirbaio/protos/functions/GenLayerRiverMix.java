package net.dirbaio.protos.functions;

public class GenLayerRiverMix extends BiomeFunction
{
    public BiomeFunction biomePattern;
    public BiomeFunction riverPattern;

    public GenLayerRiverMix(BiomeFunction biomePattern, BiomeFunction riverPattern)
    {
        this.biomePattern = biomePattern;
        this.riverPattern = riverPattern;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] var5 = this.biomePattern.getBiomeData(px, pz, sx, sz);
        int[] var6 = this.riverPattern.getBiomeData(px, pz, sx, sz);
        int[] var7 = IntCache.getIntCache(sx * sz);

        for (int var8 = 0; var8 < sx * sz; ++var8)
        {
            if (var5[var8] == Biome.ocean.biomeID)
            {
                var7[var8] = var5[var8];
            }
            else if (var6[var8] >= 0)
            {
                if (var5[var8] == Biome.icePlains.biomeID)
                {
                    var7[var8] = Biome.frozenRiver.biomeID;
                }
                else if (var5[var8] != Biome.mushroomIsland.biomeID && var5[var8] != Biome.mushroomIslandShore.biomeID)
                {
                    var7[var8] = var6[var8];
                }
                else
                {
                    var7[var8] = Biome.mushroomIslandShore.biomeID;
                }
            }
            else
            {
                var7[var8] = var5[var8];
            }
        }

        return var7;
    }
}
