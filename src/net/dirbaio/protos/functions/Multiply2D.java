package net.dirbaio.protos.functions;

public class Multiply2D extends Function2D
{
	public Function2D a, b;

	public Multiply2D()
	{
	}

	public Multiply2D(Function2D a, Function2D b)
	{
		this.a = a;
		this.b = b;
	}
		
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] da = a.get2DData(px, pz, sx, sz);
		double[][] db;
		
		if(a == b)
			db = da;
		else
			db = b.get2DData(px, pz, sx, sz);
		
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
			{
				double na = da[i][j];
				double nb = db[i][j];
				
				da[i][j] = na*nb;
			}
		return da;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
	}

}
