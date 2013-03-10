package net.dirbaio.protos.functions;

public class GetYCoord extends Function3D
{
	public GetYCoord()
	{
	}

	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = new double[sx][sy][sz];
		
		for(int x = 0; x < sx; x++)
            for(int y = 0; y < sy; y++)
                for(int z = 0; z < sz; z++)
					res[x][y][z] = y+py;
		
		return res;
	}
}
