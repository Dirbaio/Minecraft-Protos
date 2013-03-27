package net.dirbaio.protos.functions;

public class BiomeOutline extends BiomeFunction
{
    public BiomeFunction base;
    public BiomeSet biomes;
    public int outline;
    public boolean outer;
    public BiomeSet nextTo;
    
    public BiomeOutline()
    {
    }

    public BiomeOutline(BiomeFunction base, BiomeSet biomes, int outline, boolean outer, BiomeSet nextTo)
    {
        this.base = base;
        this.biomes = biomes;
        this.outline = outline;
        this.outer = outer;
        this.nextTo = nextTo;
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

                if(outer)
                {
                    if (!biomes.contains(center) 
                            && (biomes.contains(bottom) || biomes.contains(right) || biomes.contains(left) || biomes.contains(top))
                            && nextTo.contains(bottom) && nextTo.contains(right) && nextTo.contains(left) && nextTo.contains(top))
                        data[x+z*sx] = outline;
                    else
                        data[x+z*sx] = center;
                }
                else
                {
                    if (biomes.contains(center) 
                            && (bottom != center || right != center || left != center || top != center)
                            && nextTo.contains(bottom) && nextTo.contains(right) && nextTo.contains(left) && nextTo.contains(top))
                        data[x+z*sx] = outline;
                    else
                        data[x+z*sx] = center;
                }
                
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
                */
            }
        }

        return data;
    }
}
