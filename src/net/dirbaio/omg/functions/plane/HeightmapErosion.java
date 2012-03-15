package net.dirbaio.omg.functions.plane;

import net.dirbaio.omg.functions.Function2D;

public class HeightmapErosion extends Function2D
{
	Function2D base;

    public int erosionIterations = 500;
    public double erosionSlopeStart = 0.5;
    public double erosionSlopeEnd = 4;
    public int erosionSmoothFreq = 50;
    public int erosionRandFreq = 50;
    public double erosionRandSize = 50;
    public double erosionRandAmount = 0.1;

	double[][] heightmap;
	int cx, cz, csx, csz;
	
	public HeightmapErosion(Function2D base)
	{
		this.base = base;
	}
	
	
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sx][sz];
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
				res[i][j] = heightmap[px-cx+i][pz-cz+j];
		
		return res;
	}
	
	
    private double get(double[][] m, int x, int z)
    {
        x = (x+csx)%csx;
        z = (z+csz)%csz;
        return m[x][z];
    }
    
    private void add(double[][] m, int x, int z, double d)
    {
        x = (x+csx)%csx;
        z = (z+csz)%csz;
        m[x][z] += d;
    }
    private void put(double[][] m, int x, int z, double d)
    {
        x = (x+csx)%csx;
        z = (z+csz)%csz;
        m[x][z] = d;
    }
	
	@Override
	public void prepare(int xx, int zz, int sx, int sz)
	{
		base.prepare(xx, zz, sx, sz);

		cx = xx;
		cz = zz;
		csx = sx;
		csz = sz;
		
		double[][] h = base.get2DData(xx, zz, sx, sz);
		
        for(int i = 0; i < erosionIterations; i++)
        {
            double t = erosionSlopeStart + (erosionSlopeEnd-erosionSlopeStart)*i/erosionIterations;
            
             if(i % 10 == 0)
                System.out.println("Eroding" + i*100/erosionIterations + "% slope" + t);
             /*
            if(erosionRandFreq != 0 && i % erosionRandFreq == 0)
            {
                OctaveNoiseGenerator rng = new OctaveNoiseGenerator(seed+i, 3, 0.5, 0.5, erosionRandSize);
                rng.init();
                for(int x = 0; x < size; x++)
                    for(int y = 0; y < size; y++)
                        h[x][y] += rng.noise(x, y)*erosionRandAmount;
                
            }*/
            if(erosionSmoothFreq != 0 && i % erosionSmoothFreq == 0)
            {
                for(int x = 0; x < sx; x++)
                    for(int z = 0; z < sz; z++)
                    {
                        h[x][z] = (
                                get(h, x+1, z+1)
                              + get(h, x+1, z)
                              + get(h, x+1, z-1) 
                              + get(h, x, z+1)
                              + get(h, x, z)
                              + get(h, x, z-1)
                              + get(h, x-1, z+1)
                              + get(h, x-1, z)
                              + get(h, x-1, z-1)
                                )/9;
                    }
            }
            for(int x = 0; x < sx; x++)
                for(int y = 0; y < sz; y++)
                {
                    double h1 = get(h, x+1, y);
                    double h2 = get(h, x-1, y);
                    double h3 = get(h, x, y+1);
                    double h4 = get(h, x, y-1);
                    
                    double hmin = h1;
                    int xmin = x+1;
                    int ymin = y;
                    
                    if(h2 < hmin) { hmin = h2; xmin = x-1; ymin = y; }
                    if(h3 < hmin) { hmin = h3; xmin = x; ymin = y+1; }
                    if(h4 < hmin) { hmin = h4; xmin = x; ymin = y-1; }
                    
                    if(hmin > h[x][y]) continue;
                    
                    double d = h[x][y] - hmin;
                    if(d < t)
                    {
                        add(h, xmin, ymin, d/2);
                        h[x][y] -= d/2;
                    }
                }
        }
		
		heightmap = h;
	}

}
