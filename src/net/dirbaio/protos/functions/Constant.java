package net.dirbaio.protos.functions;

public class Constant extends Function2D
{
	double value;

	public Constant()
	{
	}

	public Constant(double k)
	{
		this.value = k;
    }
	
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sx][sz];
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
				res[i][j] = value;
		return res;
	}

    @Override
    public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[][] data = get2DData(px, pz, sx, sz);

		double[][][] res = new double[sx][sy][sz];
		
        for(int i = 0; i < sx; i++)
            for(int j = 0; j < sy; j++)
                for(int kk = 0; kk < sz; kk++)
    				res[i][j][kk] = value;
        
		return res;
    }
    
}
