package net.dirbaio.protos.functions;

public class GenLayerFuzzyZoom extends BiomeFunction
{
    public BiomeFunction base;
    public GenLayerFuzzyZoom(BiomeFunction base)
    {
        this.base = base;
    }

    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int px2 = px >> 1;
        int pz2 = pz >> 1;
        int sx2 = (sx >> 1) + 3;
        int sz2 = (sz >> 1) + 3;
        int[] data2 = this.base.getBiomeData(px2, pz2, sx2, sz2);
        int[] data = IntCache.getIntCache(sx2 * 2 * sz2 * 2);
        int var11 = sx2 << 1;

        for (int z = 0; z < sz2 - 1; ++z)
        {
            int var14 = (z << 1) * var11;
            int var15 = data2[0 + (z + 0) * sx2];
            int var16 = data2[0 + (z + 1) * sx2];

            for (int x = 0; x < sx2 - 1; ++x)
            {
                this.setPosForRandom(x + px2 << 1, z + pz2 << 1);
                int var18 = data2[x + 1 + (z + 0) * sx2];
                int var19 = data2[x + 1 + (z + 1) * sx2];
                data[var14] = var15;
                data[var14++ + var11] = this.choose(var15, var16);
                data[var14] = this.choose(var15, var18);
                data[var14++ + var11] = this.choose(var15, var18, var16, var19);
                var15 = var18;
                var16 = var19;
            }
        }

        int[] var20 = IntCache.getIntCache(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            System.arraycopy(data, (z + (pz & 1)) * (sx2 << 1) + (px & 1), var20, z * sx, sx);
        }

        return var20;
    }

    /**
     * randomly choose between the two args
     */
    protected int choose(int par1, int par2)
    {
        return this.nextInt(2) == 0 ? par1 : par2;
    }

    /**
     * randomly choose between the four args
     */
    protected int choose(int par1, int par2, int par3, int par4)
    {
        int var5 = this.nextInt(4);
        return var5 == 0 ? par1 : (var5 == 1 ? par2 : (var5 == 2 ? par3 : par4));
    }
}
