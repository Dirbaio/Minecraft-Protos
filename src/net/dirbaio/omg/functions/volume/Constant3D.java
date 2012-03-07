package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.*;

public class Constant3D extends Function3D
{
	double k;

	public Constant3D()
	{
	}

	public Constant3D(double k)
	{
		this.k = k;
	}
	
	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = new double[sx][sy][sz];
		
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++)
				for(int kk = 0; kk < sz; kk++)
					res[i][j][kk] = k;

		return res;
	}

}
