
package net.dirbaio.omg.functions.plane;

import net.dirbaio.omg.functions.*;

public class Slice2D extends Function2D
{
    Function3D base;
    int yy;

    public Slice2D(Function3D base, int yy)
    {
        this.base = base;
        this.yy = yy;
    }
    
    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        base.prepare(x, z, sx, sz);
    }

    @Override
    public double[][] get2DData(int px, int pz, int sx, int sz)
    {
        double[][][] data = base.get3DData(px, yy, pz, sx, 1, sz);
        double[][] res = new double[sx][sz];
        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
                res[x][z] = data[x][0][z];
        return res;
    }
    
}
