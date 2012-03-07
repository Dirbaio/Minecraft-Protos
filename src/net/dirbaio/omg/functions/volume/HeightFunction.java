package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.*;

public class HeightFunction extends Function3D
{
	Function2D f;

	public HeightFunction()
	{
	}

	public HeightFunction(Function2D f)
	{
		this.f = f;
	}
	
	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = new double[sx][sy][sz];
		double[][] hf = f.get2DData(px, pz, sx, sz);
		
		for(int x = 0; x < sx; x++)
			for(int z = 0; z < sz; z++)
			{
				double h = hf[x][z];
				for(int y = 0; y < sy; y++)
					res[x][y][z] = y-h;
			}
		
		return res;
	}

}
