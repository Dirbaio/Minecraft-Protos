package net.dirbaio.protos.functions;

public class GenLayerShore extends BiomeFunction
{
    public BiomeFunction base;
    public int biome;
    public int outline;

    public GenLayerShore()
    {
    }

    public GenLayerShore(BiomeFunction base, int biome, int outline)
    {
        this.base = base;
        this.biome = biome;
        this.outline = outline;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] data2 = base.getBiomeData(px - 1, pz - 1, sx + 2, sz + 2);
        int[] data = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            for (int x = 0; x < sx; ++x)
            {
                int center = data2[x + 1 + (z + 1) * (sx + 2)];
                int bottom = data2[x + 1 + (z + 1 - 1) * (sx + 2)];
                int right = data2[x + 1 + 1 + (z + 1) * (sx + 2)];
                int left = data2[x + 1 - 1 + (z + 1) * (sx + 2)];
                int top = data2[x + 1 + (z + 1 + 1) * (sx + 2)];

                if (center == biome && (bottom != biome || right != biome || left != biome || top != biome))
                    data[x+z*sx] = outline;
                else
                    data[x+z*sx] = center;
                /*
                if (center == Biome.mushroomIsland.biomeID)
                {
                    if (bottom != Biome.ocean.biomeID && right != Biome.ocean.biomeID && left != Biome.ocean.biomeID && top != Biome.ocean.biomeID)
                        data[x + z * sx] = center;
                    else
                        data[x + z * sx] = Biome.mushroomIslandShore.biomeID;
                }
                else if (center != Biome.ocean.biomeID && center != Biome.river.biomeID && center != Biome.swampland.biomeID && center != Biome.extremeHills.biomeID)
                {
                    if (bottom != Biome.ocean.biomeID && right != Biome.ocean.biomeID && left != Biome.ocean.biomeID && top != Biome.ocean.biomeID)
                        data[x + z * sx] = center;
                    else
                        data[x + z * sx] = Biome.beach.biomeID;
                }
                else if (center == Biome.extremeHills.biomeID)
                {
                    if (bottom == Biome.extremeHills.biomeID && right == Biome.extremeHills.biomeID && left == Biome.extremeHills.biomeID && top == Biome.extremeHills.biomeID)
                        data[x + z * sx] = center;
                    else
                        data[x + z * sx] = Biome.extremeHillsEdge.biomeID;
                }
                else
                    data[x + z * sx] = center;
                    * */
            }
        }

        return data;
    }
}
