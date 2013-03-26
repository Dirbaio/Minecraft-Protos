package net.dirbaio.protos.functions;

public class GenLayerAddIsland extends BiomeFunction
{
    public BiomeFunction base;
    
    public GenLayerAddIsland(BiomeFunction parent)
    {
        this.base = parent;
    }

    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int px2 = px - 1;
        int pz2 = pz - 1;
        int sx2 = sx + 2;
        int sz2 = sz + 2;
        int[] base = this.base.getBiomeData(px2, pz2, sx2, sz2);
        int[] data = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            for (int x = 0; x < sx; ++x)
            {
                int topLeft     = base[x + 0 + (z + 0) * sx2];
                int topRight    = base[x + 2 + (z + 0) * sx2];
                int bottomLeft  = base[x + 0 + (z + 2) * sx2];
                int bottomRight = base[x + 2 + (z + 2) * sx2];
                int center      = base[x + 1 + (z + 1) * sx2];

                if (center == 0 && (topLeft != 0 || topRight != 0 || bottomLeft != 0 || bottomRight != 0))
                {
                    int max = 1;
                    int r = 1;

                    //Choose randomly from the 4 values one that's not 0
                    if (    topLeft != 0 && this.randForPos(max++, x + px, z + pz, 0) == 0) r = topLeft;
                    if (   topRight != 0 && this.randForPos(max++, x + px, z + pz, 1) == 0) r = topRight;
                    if ( bottomLeft != 0 && this.randForPos(max++, x + px, z + pz, 2) == 0) r = bottomLeft;
                    if (bottomRight != 0 && this.randForPos(max++, x + px, z + pz, 3) == 0) r = bottomRight;

                    if (this.randForPos(3, x + px, z + pz, 4) == 0)
                        data[x + z * sx] = r;
                    else if (r == Biome.icePlains.biomeID)
                        data[x + z * sx] = Biome.frozenOcean.biomeID;
                    else
                        data[x + z * sx] = 0;
                }
                else if (center > 0 && (topLeft == 0 || topRight == 0 || bottomLeft == 0 || bottomRight == 0))
                {
                    if (this.randForPos(5, x + px, z + pz, 5) == 0)
                    {
                        if (center == Biome.icePlains.biomeID)
                            data[x + z * sx] = Biome.frozenOcean.biomeID;
                        else
                            data[x + z * sx] = 0;
                    }
                    else
                        data[x + z * sx] = center;
                }
                else
                    data[x + z * sx] = center;
            }
        }

        return data;
    }
}
