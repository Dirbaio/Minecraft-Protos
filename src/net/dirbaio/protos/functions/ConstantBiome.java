package net.dirbaio.protos.functions;

import java.util.Arrays;

public class ConstantBiome extends BiomeFunction
{
    int biome;

    public ConstantBiome()
    {
    }

    public ConstantBiome(int biome)
    {
        this.biome = biome;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] res = ArrayCache.newInt(sx * sz);
        Arrays.fill(res, biome);
        return res;
    }
}
