package net.dirbaio.protos.functions;

public class BiomeCombine extends BiomeFunction
{
    public BiomeFunction a, b;
    public BiomeSet sa, sb;
    public int res;

    public BiomeCombine()
    {
    }

    public BiomeCombine(BiomeFunction a, BiomeFunction b, BiomeSet sa, BiomeSet sb, int res)
    {
        this.a = a;
        this.b = b;
        this.sa = sa;
        this.sb = sb;
        this.res = res;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] da = a.getBiomeData(px, pz, sx, sz);
        int[] db = b.getBiomeData(px, pz, sx, sz);
        
        for(int i = 0; i < sx*sz; i++)
        {
            if(sa.contains(da[i]) && sb.contains(db[i]))
            {
                if(res == -1)
                    da[i] = db[i];
                else
                    da[i] = res;
            }
        }
        
        return da;
    }
    
}
