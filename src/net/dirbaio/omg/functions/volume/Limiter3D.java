package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.*;

public class Limiter3D extends Function3D
{
	Function3D base;
	double minY, maxY;
	double margin;

    public Limiter3D()
    {
    }
	
	public Limiter3D(Function3D base, double minY, double maxY)
	{
		this.base = base;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = base.get3DData(px, py, pz, sx, sy, sz);
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++)
				for(int k = 0; k < sz; k++)
				{
                    if(j > maxY-margin)
						res[i][j][k] -= (j-maxY+margin)/margin;
				}
		return res;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
}
