package net.dirbaio.protos.functions;

import java.util.ArrayList;

public class BiomeRandomReplace extends BiomeFunction
{
    public BiomeFunction base;
    public BiomeSet from;
    public boolean noEdges; 
    public ArrayList<BiomeProb> to;

    public BiomeRandomReplace()
    {
        to = new ArrayList<>();
    }

    public BiomeRandomReplace(BiomeFunction base, BiomeSet from, ArrayList<BiomeProb> to)
    {
        this.base = base;
        this.from = from;
        this.to = to;
    }

    public BiomeRandomReplace(BiomeFunction base, BiomeSet from, boolean noEdges, ArrayList<BiomeProb> to)
    {
        this.base = base;
        this.from = from;
        this.noEdges = noEdges;
        this.to = to;
    }
    
    public BiomeRandomReplace(BiomeFunction base, BiomeSet from, boolean noEdges, int to, int prob)
    {
        this.base = base;
        this.from = from;
        this.noEdges = noEdges;
        
        this.to = new ArrayList<>();
        this.to.add(new BiomeProb(to, 1));
        this.to.add(new BiomeProb(-1, prob - 1));
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        //TODO Maybe optimize the list scanning to an array lookup?
        
        int s = 0;
        for(BiomeProb p : to)
            s += p.prob;
        
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
                    {
                        int r = this.randForPos(s, px + x, pz + z, 0);
                        for(BiomeProb p : to)
                        {
                            r -= p.prob;
                            if(r < 0)
                            {
                                if(p.biome != -1)
                                    val = p.biome;
                                break;
                            }
                        }
                    }
                    
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
                    {
                        int r = this.randForPos(s, px + x, pz + z, 0);
                        for(BiomeProb p : to)
                        {
                            r -= p.prob;
                            if(r < 0)
                            {
                                if(p.biome != -1)
                                    res[x + z * sx] = p.biome;
                                break;
                            }
                        }
                    }

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
