package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.Function3D;

public class OverhangFunction extends Function3D
{
	double height;
	double strength;

    public OverhangFunction()
    {
    }

	public OverhangFunction(double height, double strength)
	{
		this.height = height;
		this.strength = strength;
	}

	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = new double[sx][sy][sz];

		double widesq = 16;
		
		for (int y = 0; y < sy; y++)
		{
			double val = Math.pow(Math.E, -(y-height)*(y-height)/widesq) * strength;
			for (int x = 0; x < sx; x++)
				for (int z = 0; z < sz; z++)
					res[x][y][z] = val;
		}
		
		return res;
	}
	
	
}
