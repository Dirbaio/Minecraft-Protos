package net.dirbaio.protos.functions;

public class GenLayerBiome extends BiomeFunction
{
    public BiomeFunction base;

    /** this sets all the biomes that are allowed to appear in the overworld */
    private Biome[] allowedBiomes;

    public GenLayerBiome(BiomeFunction base)
    {
        this.allowedBiomes = new Biome[] {
            Biome.desert, 
            Biome.forest, 
            Biome.extremeHills, 
            Biome.swampland, 
            Biome.plains, 
            Biome.taiga, 
            Biome.jungle
        };
        
        this.base = base;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] data2 = this.base.getBiomeData(px, pz, sx, sz);
        int[] data = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            for (int x = 0; x < sx; ++x)
            {
                int val = data2[x + z * sx];

                if (val == 0)
                    data[x + z * sx] = 0;
                else if (val == Biome.mushroomIsland.biomeID)
                    data[x + z * sx] = val;
                else if (val == 1)
                    data[x + z * sx] = this.allowedBiomes[this.randForPos(this.allowedBiomes.length, x + px, z + pz, 0)].biomeID;
                else
                {
                    int val2 = this.allowedBiomes[this.randForPos(this.allowedBiomes.length, x + px, z + pz, 1)].biomeID;

                    if (val2 == Biome.taiga.biomeID)
                        data[x + z * sx] = val2;
                    else
                        data[x + z * sx] = Biome.icePlains.biomeID;
                }
            }
        }

        return data;
    }
}
