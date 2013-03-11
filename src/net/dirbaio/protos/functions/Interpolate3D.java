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

public class Interpolate3D extends Function3D
{
	public Function3D a, b;
	public Function3D weight;

	public Interpolate3D(Function3D a, Function3D b, Function3D weight)
	{
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] sa = a.get3DData(px, py, pz, sx, sy, sz);
		double[][][] sb = b.get3DData(px, py, pz, sx, sy, sz);
		double[][][] sw = weight.get3DData(px, py, pz, sx, sy, sz);
		
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++)
				for(int k = 0; k < sz; k++)
				{
					double w = sw[i][j][k];
					if(w < 0) w = 0;
					if(w > 1) w = 1;
					
					double va = sa[i][j][k];
					double vb = sb[i][j][k];
					
					sa[i][j][k] = va * w + vb*(1-w);
				}
		
		return sa;
	}
	
	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
		weight.prepare(x, z, sx, sz);
	}
}
