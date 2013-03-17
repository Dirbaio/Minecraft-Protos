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

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Function2D extends Function3D
{
	public abstract double[] get2DData(int px, int pz, int sx, int sz);

    @Override
    public double[] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[] data = get2DData(px, pz, sx, sz);
		double[] res = new double[sx*sy*sz];
		
        int s = sx*sz;
        for(int y = 0; y < sy; y++)
            System.arraycopy(data, 0, res, y*s, s);
        
		return res;
    }
    
    public BufferedImage render(int px, int pz, int sx, int sz, double min, double max)
    {
        prepare(px, pz, sx, sz);
        return renderPrepared(px, pz, sx, sz, min, max);
    }
    
    public BufferedImage renderPrepared(int px, int pz, int sx, int sz, double min, double max)
    {
        BufferedImage res = new BufferedImage(sx, sz, BufferedImage.TYPE_INT_RGB);
        double[] data = get2DData(px, pz, sx, sz);
        
        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
            {
                double val = data[x*sz + z];
                val = (val-min)/(max-min)*256;
                int shade = (int) val;
                int col = shade | (shade<<8) | (shade<<16);
                if(shade < 0 || shade > 255)
                    col = 0x000000FF;
                res.setRGB(x, z, col);
            }
        return res;
    }
    
    public BufferedImage renderChunks(int cx, int cz, int sx, int sz, double min, double max)
    {
        prepare(cx*16, cz*16, sx*16, sz*16);

        BufferedImage res = new BufferedImage(sx*16, sz*16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = res.createGraphics();

        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
                g.drawImage(renderPrepared((cx+x)*16, (cz+z)*16, 16, 16, min, max), x*16, z*16, null);

        return res;
    }
}
