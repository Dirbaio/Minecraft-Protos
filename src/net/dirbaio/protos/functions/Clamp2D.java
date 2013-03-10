package net.dirbaio.protos.functions;

public class Clamp2D extends Function2D
{

    public Function2D f;
    public double min, max;
    
    public Clamp2D()
    {
    }

    public Clamp2D(Function2D f, double min, double max)
    {
        this.f = f;
        this.min = min;
        this.max = max;
    }

    @Override
    public double[][] get2DData(int px, int pz, int sx, int sz)
    {
        double[][] d = f.get2DData(px, pz, sx, sz);

        for (int i = 0; i < sx; i++)
                for (int k = 0; k < sz; k++)
                {
                    double n = d[i][k];
                    if(n < min) n = min;
                    if(n > max) n = max;
                    
                    d[i][k] = n;
                }

        return d;
    }

    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        f.prepare(x, z, sx, sz);
    }
}
