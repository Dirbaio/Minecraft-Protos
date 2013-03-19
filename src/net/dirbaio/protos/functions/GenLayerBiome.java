package net.dirbaio.protos.functions;

public class GenLayerBiome extends BiomeFunction
{
    public BiomeFunction base;

    /** this sets all the biomes that are allowed to appear in the overworld */
    private Biome[] allowedBiomes;

    public GenLayerBiome(BiomeFunction base)
    {
        this.allowedBiomes = new Biome[] {Biome.desert, Biome.forest, Biome.extremeHills, Biome.swampland, Biome.plains, Biome.taiga, Biome.jungle};
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
                int var9 = var5[var8 + var7 * sx];

                if (var9 == 0)
                {
                    var6[var8 + var7 * sx] = 0;
                }
                else if (var9 == Biome.mushroomIsland.biomeID)
                {
                    var6[var8 + var7 * sx] = var9;
                }
                else if (var9 == 1)
                {
                    var6[var8 + var7 * sx] = this.allowedBiomes[this.nextInt(this.allowedBiomes.length)].biomeID;
                }
                else
                {
                    int var10 = this.allowedBiomes[this.nextInt(this.allowedBiomes.length)].biomeID;

                    if (var10 == Biome.taiga.biomeID)
                    {
                        var6[var8 + var7 * sx] = var10;
                    }
                    else
                    {
                        var6[var8 + var7 * sx] = Biome.icePlains.biomeID;
                    }
                }
            }
        }

        return var6;
    }
}
