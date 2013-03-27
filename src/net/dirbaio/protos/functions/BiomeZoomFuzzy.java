package net.dirbaio.protos.functions;

public class BiomeZoomFuzzy extends BiomeFunction
{
    public BiomeFunction base;
    public BiomeZoomFuzzy(BiomeFunction base)
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
        int[] data = ArrayCache.newInt(sx2 * 2 * sz2 * 2);
        int var11 = sx2 << 1;

        for (int z = 0; z < sz2 - 1; ++z)
        {
            int var14 = (z << 1) * var11;
            int var15 = data2[0 + (z + 0) * sx2];
            int var16 = data2[0 + (z + 1) * sx2];

            for (int x = 0; x < sx2 - 1; ++x)
            {
                int var18 = data2[x + 1 + (z + 0) * sx2];
                int var19 = data2[x + 1 + (z + 1) * sx2];
                data[var14] = var15;
                data[var14++ + var11] = this.choose(var15, var16, x + px2 << 1, z + pz2 << 1, 0);
                data[var14] = this.choose(var15, var18, x + px2 << 1, z + pz2 << 1, 1);
                data[var14++ + var11] = this.choose(var15, var18, var16, var19, x + px2 << 1, z + pz2 << 1, 2);
                var15 = var18;
                var16 = var19;
            }
        }

        int[] var20 = ArrayCache.newInt(sx * sz);

        for (int z = 0; z < sz; ++z)
        {
            System.arraycopy(data, (z + (pz & 1)) * (sx2 << 1) + (px & 1), var20, z * sx, sx);
        }

        return var20;
    }

    /**
     * randomly choose between the two args
     */
    protected int choose(int par1, int par2, int x, int z, int n)
    {
        return this.randForPos(2, x, z, n) == 0 ? par1 : par2;
    }

    /**
     * randomly choose between the four args
     */
    protected int choose(int par1, int par2, int par3, int par4, int x, int z, int n)
    {
        int var5 = this.randForPos(4, x, z, n);
        return var5 == 0 ? par1 : (var5 == 1 ? par2 : (var5 == 2 ? par3 : par4));
    }
}
