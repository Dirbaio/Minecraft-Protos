package net.dirbaio.protos.functions;

public class GenLayerHills extends BiomeFunction
{
    public BiomeFunction base;
    public GenLayerHills(BiomeFunction base)
    {
        this.base = base;
    }

    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int[] data2 = this.base.getBiomeData(px - 1, pz - 1, sx + 2, sz + 2);
        int[] data = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            for (int x = 0; x < sx; ++x)
            {
                int old = data2[x + 1 + (z + 1) * (sx + 2)];

                if (this.randForPos(3, x + px, z + pz, 0) == 0)
                {
                    int curr = old;

                    if (old == Biome.desert.biomeID)
                        curr = Biome.desertHills.biomeID;
                    else if (old == Biome.forest.biomeID)
                        curr = Biome.forestHills.biomeID;
                    else if (old == Biome.taiga.biomeID)
                        curr = Biome.taigaHills.biomeID;
                    else if (old == Biome.plains.biomeID)
                        curr = Biome.forest.biomeID;
                    else if (old == Biome.icePlains.biomeID)
                        curr = Biome.iceMountains.biomeID;
                    else if (old == Biome.jungle.biomeID)
                        curr = Biome.jungleHills.biomeID;

                    if (curr == old)
                        data[x + z * sx] = old;
                    else
                    {
                        int top = data2[x + 1 + (z + 1 - 1) * (sx + 2)];
                        int right = data2[x + 1 + 1 + (z + 1) * (sx + 2)];
                        int left = data2[x + 1 - 1 + (z + 1) * (sx + 2)];
                        int down = data2[x + 1 + (z + 1 + 1) * (sx + 2)];

                        if (top == old && right == old && left == old && down == old)
                            data[x + z * sx] = curr;
                        else
                            data[x + z * sx] = old;
                    }
                }
                else
                    data[x + z * sx] = old;
            }
        }

        return data;
    }
}
