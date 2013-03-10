package net.dirbaio.protos.functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image2D extends Function2D
{
    BufferedImage bi;
    String path;
    int x0, z0;
    public Image2D(String path)
    {
        this.path = path;
    }
    
    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        x0 = x;
        z0 = z;

        try
        {
            bi = ImageIO.read(new File(path));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public double[][] get2DData(int px, int pz, int sx, int sz)
    {
        double[][] r = new double[sx][sz];
        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
            {
                int val = bi.getRGB(x+px-x0, z+pz-z0) & 0xFF;
                r[x][z] = (val-128);
            }
        return r;
    }
}
