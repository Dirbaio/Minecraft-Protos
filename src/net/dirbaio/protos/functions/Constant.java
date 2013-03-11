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

public class Constant extends Function2D
{
	double value;

	public Constant()
	{
	}

	public Constant(double k)
	{
		this.value = k;
    }
	
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sx][sz];
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
				res[i][j] = value;
		return res;
	}

    @Override
    public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[][] data = get2DData(px, pz, sx, sz);

		double[][][] res = new double[sx][sy][sz];
		
        for(int i = 0; i < sx; i++)
            for(int j = 0; j < sy; j++)
                for(int kk = 0; kk < sz; kk++)
    				res[i][j][kk] = value;
        
		return res;
    }
    
}
