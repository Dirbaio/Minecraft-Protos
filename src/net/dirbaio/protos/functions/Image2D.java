/*
 * Copyright (C) 2013 dirbaio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.dirbaio.protos.functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image2D extends Function2D
{
    BufferedImage bi;
    public String path;
    public int x0, z0;

    public Image2D()
    {
    }
    
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
