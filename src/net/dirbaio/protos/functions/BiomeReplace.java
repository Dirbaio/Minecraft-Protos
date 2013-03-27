package net.dirbaio.protos.functions;

import java.util.ArrayList;

public class BiomeReplace extends BiomeFunction
{
    public BiomeFunction base;
    public BiomeSet from;
    public int to;
    public boolean noEdges; 

    public BiomeReplace()
    {
    }

    public BiomeReplace(BiomeFunction base, BiomeSet from, int to, boolean noEdges)
    {
        this.base = base;
        this.from = from;
        this.to = to;
        this.noEdges = noEdges;
    }

    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        if(noEdges)
        {
            int[] data2 = base.getBiomeData(px - 1, pz - 1, sx + 2, sz + 2);
            int[] data = ArrayCache.newInt(sx * sz);

            for (int z = 0; z < sz; ++z)
            {
                for (int x = 0; x < sx; ++x)
                {
                    int old = data2[x + 1 + (z + 1) * (sx + 2)];
                    int val = old;

                    if(from.contains(val))
                        val = to;
                    
                    if (val == old)
                        data[x + z * sx] = old;
                    else
                    {
                        int top = data2[x + 1 + (z + 1 - 1) * (sx + 2)];
                        int right = data2[x + 1 + 1 + (z + 1) * (sx + 2)];
                        int left = data2[x + 1 - 1 + (z + 1) * (sx + 2)];
                        int down = data2[x + 1 + (z + 1 + 1) * (sx + 2)];

                        if (top == old && right == old && left == old && down == old)
                            data[x + z * sx] = val;
                        else
                            data[x + z * sx] = old;
                    }
                }
            }

            return data;
        }
        else
        {
            int[] res = base.getBiomeData(px, pz, sx, sz);

            for (int z = 0; z < sz; ++z)
                for (int x = 0; x < sx; ++x)
                    if(from.contains(res[x + z * sx]))
                        res[x + z * sx] = to;

            return res;
        }
    }
    
    public static class BiomeProb
    {
        int biome;
        int prob;

        public BiomeProb()
        {
        }

        public BiomeProb(int biome, int prob)
        {
            this.biome = biome;
            this.prob = prob;
        }

    }
}
