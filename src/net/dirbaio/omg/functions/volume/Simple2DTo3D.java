package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.*;

public class Simple2DTo3D extends Function3D
{
    Function2D base;

    public Simple2DTo3D(Function2D base)
    {
        this.base = base;
    }

    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        base.prepare(x, z, sx, sz);
    }
    
    @Override
    public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
		double[][][] res = new double[sx][sy][sz];
        double[][] wtf = base.get2DData(px, pz, sx, sz);
        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
            {
                double val = wtf[x][z];
                for(int y = 0; y < sy; y++)
                    res[x][y][z] = val;
            }
        return res;
    }
    
}
