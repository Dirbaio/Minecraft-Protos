package net.dirbaio.protos.functions;

public class GenLayerZoom extends BiomeFunction
{
    public BiomeFunction base;

    public GenLayerZoom(BiomeFunction base)
    {
        this.base = base;
    }
    
    @Override
    public int[] getBiomeData(int px, int pz, int sx, int sz)
    {
        int var5 = px >> 1;
        int var6 = pz >> 1;
        int var7 = (sx >> 1) + 3;
        int var8 = (sz >> 1) + 3;
        int[] var9 = this.base.getBiomeData(var5, var6, var7, var8);
        int[] var10 = IntCache.getIntCache(var7 * 2 * var8 * 2);
        int var11 = var7 << 1;
        int var13;

        for (int var12 = 0; var12 < var8 - 1; ++var12)
        {
            var13 = var12 << 1;
            int var14 = var13 * var11;
            int var15 = var9[0 + (var12 + 0) * var7];
            int var16 = var9[0 + (var12 + 1) * var7];

            for (int var17 = 0; var17 < var7 - 1; ++var17)
            {
                int var18 = var9[var17 + 1 + (var12 + 0) * var7];
                int var19 = var9[var17 + 1 + (var12 + 1) * var7];
                var10[var14] = var15;
                var10[var14++ + var11] = this.choose(var15, var16, var17 + var5 << 1, var12 + var6 << 1, 0);
                var10[var14] = this.choose(var15, var18, var17 + var5 << 1, var12 + var6 << 1, 0);
                var10[var14++ + var11] = this.modeOrRandom(var15, var18, var16, var19, var17 + var5 << 1, var12 + var6 << 1, 0);
                var15 = var18;
                var16 = var19;
            }
        }

        int[] var20 = IntCache.getIntCache(sx * sz);

        for (var13 = 0; var13 < sz; ++var13)
        {
            System.arraycopy(var10, (var13 + (pz & 1)) * (var7 << 1) + (px & 1), var20, var13 * sx, sx);
        }

        return var20;
    }

    /**
     * Chooses one of the two inputs randomly.
     */
    protected int choose(int par1, int par2, int x, int z, int n)
    {
        return this.randForPos(2, x, z, n) == 0 ? par1 : par2;
    }

    /**
     * returns the mode (most frequently occuring number) or a random number from the 4 integers provided
     */
    protected int modeOrRandom(int par1, int par2, int par3, int par4, int x, int z, int n)
    {
        if (par2 == par3 && par3 == par4) return par2;
        else if (par1 == par2 && par1 == par3) return par1;
        else if (par1 == par2 && par1 == par4) return par1;
        else if (par1 == par3 && par1 == par4) return par1;
        else if (par1 == par2 && par3 != par4) return par1;
        else if (par1 == par3 && par2 != par4) return par1;
        else if (par1 == par4 && par2 != par3) return par1;
        else if (par2 == par1 && par3 != par4) return par2;
        else if (par2 == par3 && par1 != par4) return par2;
        else if (par2 == par4 && par1 != par3) return par2;
        else if (par3 == par1 && par2 != par4) return par3;
        else if (par3 == par2 && par1 != par4) return par3;
        else if (par3 == par4 && par1 != par2) return par3;
        else if (par4 == par1 && par2 != par3) return par3;
        else if (par4 == par2 && par1 != par3) return par3;
        else if (par4 == par3 && par1 != par2) return par3;
        else
        {
            int var5 = this.randForPos(4, x, z, n);
            return var5 == 0 ? par1 : (var5 == 1 ? par2 : (var5 == 2 ? par3 : par4));
        }
    }

    public static BiomeFunction multiZoom(BiomeFunction f, int n)
    {
        for (int i = 0; i < n; ++i)
            f = new GenLayerZoom(f);

        return f;
    }
}
