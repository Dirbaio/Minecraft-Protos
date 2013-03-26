package net.dirbaio.protos.functions;

public class GenLayerAddSnow extends BiomeFunction
{
    public BiomeFunction base;
    public GenLayerAddSnow(BiomeFunction base)
    {
        this.base = base;
    }

    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int px2 = px - 1;
        int pz2 = pz - 1;
        int sx2 = sx + 2;
        int sz2 = sz + 2;
        int[] data2 = this.base.getBiomeData(px2, pz2, sx2, sz2);
        int[] data = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            for (int x = 0; x < sx; ++x)
            {
                int val = data2[x + 1 + (z + 1) * sx2];
                if (val == 0)
                    data[x + z * sx] = 0;
                else
                {
                    int var14 = this.randForPos(5, x + px, z + pz, 0);

                    if (var14 == 0)
                        var14 = Biome.icePlains.biomeID;
                    else
                        var14 = 1;

                    data[x + z * sx] = var14;
                }
            }
        }

        return data;
    }
}
