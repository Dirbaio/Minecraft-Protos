package net.dirbaio.omg.functions.plane;

import net.dirbaio.omg.functions.*;

public class Constant2D extends Function2D
{
	double k;

	public Constant2D()
	{
	}

	public Constant2D(double k)
	{
		this.k = k;
	}
	
	
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sz][sz];
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
				res[i][j] = k;
		return res;
	}

}
