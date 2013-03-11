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

public class Limiter3D extends Function3D
{
	public Function3D base;
	public double minY, maxY;
	public double margin;

    public Limiter3D()
    {
    }
	
	public Limiter3D(Function3D base, double minY, double maxY)
	{
		this.base = base;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] res = base.get3DData(px, py, pz, sx, sy, sz);
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++)
				for(int k = 0; k < sz; k++)
				{
                    if(j > maxY-margin)
						res[i][j][k] -= (j-maxY+margin)/margin;
				}
		return res;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
}