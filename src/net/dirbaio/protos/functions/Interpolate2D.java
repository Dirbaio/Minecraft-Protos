package net.dirbaio.protos.functions;

public class Interpolate2D extends Function2D
{
	Function2D a, b;
	Function2D weight;

	public Interpolate2D(Function2D a, Function2D b, Function2D weight)
	{
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] sa = a.get2DData(px, pz, sx, sz);
		double[][] sb = b.get2DData(px, pz, sx, sz);
		double[][] sw = weight.get2DData(px, pz, sx, sz);
		
		for(int i = 0; i < sx; i++)
				for(int k = 0; k < sz; k++)
				{
					double w = sw[i][k];
					if(w < 0) w = 0;
					if(w > 1) w = 1;
					
					double va = sa[i][k];
					double vb = sb[i][k];
					
					sa[i][k] = va * w + vb*(1-w);
				}
		
		return sa;
	}
	
	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
		weight.prepare(x, z, sx, sz);
	}
}
