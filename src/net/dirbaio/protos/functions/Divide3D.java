package net.dirbaio.protos.functions;

public class Divide3D extends Function3D
{

    public Function3D a, b;

    public Divide3D()
    {
    }

    public Divide3D(Function3D a, Function3D b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[][][] da = a.get3DData(px, py, pz, sx, sy, sz);
        double[][][] db;

        if (a == b)
            db = da;
        else
            db = b.get3DData(px, py, pz, sx, sy, sz);

        for (int i = 0; i < sx; i++)
            for (int j = 0; j < sy; j++)
                for (int k = 0; k < sz; k++)
                {
                    double na = da[i][j][k];
                    double nb = db[i][j][k];

                    da[i][j][k] = na / nb;
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
