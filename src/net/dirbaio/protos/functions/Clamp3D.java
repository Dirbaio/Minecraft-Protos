package net.dirbaio.protos.functions;

public class Clamp3D extends Function3D
{

    public Function3D f;
    public double min, max;
    
    public Clamp3D()
    {
    }

    public Clamp3D(Function3D f, double min, double max)
    {
        this.f = f;
        this.min = min;
        this.max = max;
    }

    @Override
    public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[][][] d = f.get3DData(px, py, pz, sx, sy, sz);

        for (int i = 0; i < sx; i++)
            for (int j = 0; j < sy; j++)
                for (int k = 0; k < sz; k++)
                {
                    double n = d[i][j][k];
                    if(n < min) n = min;
                    if(n > max) n = max;
                    
                    d[i][j][k] = n;
                }

        return d;
    }

    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        f.prepare(x, z, sx, sz);
    }
}
