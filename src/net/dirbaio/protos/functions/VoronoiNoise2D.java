package net.dirbaio.protos.functions;

import java.util.Random;

public class VoronoiNoise2D extends Function2D
{

    private double[][][][][] grid;
    private Random r;
    private int density;
    private int size;
    private int zsize;
    private int dimensions;
    private boolean is2D;
    private DistanceMetric metric;
    private int level;

    public enum DistanceMetric {Linear, Squared, Manhattan, Quadratic, Chebyshev, Wiggly}

    private float xscale, zscale;
    
    public VoronoiNoise2D(float xscale, float zscale, int size, int density, DistanceMetric metric, int level)
    {
		this.xscale = xscale;
		this.zscale = zscale;

        is2D = true;
        zsize = (is2D ? 1 : size);
        dimensions = (is2D ? 2 : 3);
        grid = new double[size][size][zsize][density][dimensions];
        r = new Random(RandomSeed.get());
        this.size = size;
        this.density = density;
        this.metric = metric;
        this.level = level;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < zsize; k++)
                    for (int d = 0; d < density; d++)
                        for (int e = 0; e < dimensions; e++)
                            grid[i][j][k][d][e] = r.nextDouble();
    }

    private double distance(double[] a, int[] offset, double[] b)
    {
        double [] m = new double[dimensions];
        for (int i = 0; i < dimensions; i++)
            m[i] = b[i] - (a[i] + offset[i]);

        double d = 0.f;
        switch (metric) {
            case Linear:
                for (int i = 0; i < dimensions; i++)
                    d += m[i] * m[i];
                return (double) Math.sqrt(d);
            case Squared: //Linear Squared (needs special care of gain) try .05f
                for (int i = 0; i < dimensions; i++)
                    d += m[i] * m[i];
                return d;
            case Manhattan:
                for (int i = 0; i < dimensions; i++)
                    d += Math.abs(m[i]);
                return d;
            case Chebyshev:
                for (int i = 0; i < dimensions; i++)
                    d = Math.max(Math.abs(m[i]), d);
                return d;
            case Quadratic: //quadratic try .08f for gain
                for (int i = 0; i < dimensions; i++)
                    for (int j = i; j < dimensions; j++)
                        d += m[i] * m[j];
                return d;
            case Wiggly:
                for (int i = 0; i < dimensions; i++)
                    d += Math.pow(Math.abs(m[i]), 15.f);
                return (double) Math.pow(d, 1.f/15.f);
            default:
                return Double.POSITIVE_INFINITY;
        }
    }

    static final int order3D[][] = {
        {0,0,0}, {1,0,0}, {0,1,0}, {0,0,1}, {-1,0,0}, {0,-1,0}, {0,0,-1},
        {1,1,0}, {1,0,1}, {0,1,1}, {-1,1,0}, {-1,0,1}, {0,-1,1},
        {1,-1,0}, {1,0,-1}, {0,1,-1}, {-1,-1,0}, {-1,0,-1}, {0,-1,-1},
        {1,1,1}, {-1,1,1}, {1,-1,1}, {1,1,-1}, {-1,-1,1}, {-1,1,-1},
        {1,-1,-1}, {-1,-1,-1}
    };
    static final int order2D[][] = {
        {0,0}, {1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1}
    };

    
    public double get(double xin, double yin, double zin)
    {
        if (is2D)
            throw new UnsupportedOperationException(
                "Cannot create 3D Voronoi basis when instantiated with is2D = true.");
        int [] cell = { fastfloor(xin), fastfloor(yin), fastfloor(zin) };
        double [] pos = { xin - cell[0], yin - cell[1], zin - cell[2] };
        for (int i = 0; i < 3; i++) cell[i] %= size;

        double distances[] = new double[level];
        for (int i = 0; i < level; i++) distances[i] = Double.MAX_VALUE;
        for (int i = 0; i < order3D.length; i++)
        {
            boolean possible = true;
            double farDist = distances[level-1];
            if (farDist < Double.MAX_VALUE)
                for (int j = 0; j < 3; j++)
                    if (order3D[i][j] < 0 && farDist < pos[j] ||
                        order3D[i][j] > 0 && farDist < 1 - pos[j]) {
                        possible = false;
                        break;
                    }
            if (!possible) continue;
            int cx = (order3D[i][0] + cell[0]) % size;
            int cy = (order3D[i][1] + cell[1]) % size;
            int cz = (order3D[i][2] + cell[2]) % size;
            for (int j = 0; j < density; j++) {
                double d = distance(grid[cx][cy][cz][j], order3D[i], pos);
                for (int k = 0; k < level; k++) {
                    if (d < distances[k]) {
                        for (int l = level-1; l > k; l--)
                            distances[l] = distances[l-1];
                        distances[k] = d;
                        break;
                    }
                }
            }
        }
        return distances[level-1];
    }

   
    public double get(double xin, double yin)
    {
        if (!is2D)
            throw new UnsupportedOperationException(
                "Cannot create 2D Voronoi basis when instantiated with is2D = false.");

        xin /= xscale;
        yin /= zscale;
        
        int [] cell = { fastfloor(xin), fastfloor(yin) };
        double [] pos = { xin - cell[0], yin - cell[1] };
        for (int i = 0; i < 2; i++) cell[i] %= size;

        double distances[] = new double[level];
        for (int i = 0; i < level; i++) distances[i] = Double.MAX_VALUE;
        for (int i = 0; i < order2D.length; i++)
        {
            boolean possible = true;
            double farDist = distances[level-1];
            if (farDist < Double.MAX_VALUE)
                for (int j = 0; j < dimensions; j++)
                    if (order2D[i][j] < 0 && farDist < pos[j] ||
                        order2D[i][j] > 0 && farDist < 1 - pos[j]) {
                        possible = false;
                        break;
                    }
            if (!possible) continue;
            int cx = (order2D[i][0] + cell[0] + size) % size;
            int cy = (order2D[i][1] + cell[1] + size) % size;
            for (int j = 0; j < density; j++) {
                double d = distance(grid[cx][cy][0][j], order2D[i], pos);
                for (int k = 0; k < level; k++) {
                    if (d < distances[k]) {
                        for (int l = level-1; l > k; l--)
                            distances[l] = distances[l-1];
                        distances[k] = d;
                        break;
                    }
                }
            }
        }
        return distances[level-1];
    }
    
    private int fastfloor(double x)
    {
        return x > 0 ? (int) x : (int) x - 1;
    }
    
    
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sx][sz];

		for (int x = 0; x < sx; x++)
			for (int z = 0; z < sz; z++)
			{
                res[x][z] = get(x+px, z+pz);
            }
        return res;
    }
}