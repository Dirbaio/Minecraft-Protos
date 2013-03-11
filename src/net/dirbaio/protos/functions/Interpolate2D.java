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

public class Interpolate2D extends Function2D
{
	public Function2D a, b;
	public Function2D weight;

	public Interpolate2D(Function2D a, Function2D b, Function2D weight)
	{
		this.a = a;
		this.b = b;
		this.weight = weight;
	}

	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] sa = a.get2DData(px, pz, sx, sz);
		double[][] sb = b.get2DData(px, pz, sx, sz);
		double[][] sw = weight.get2DData(px, pz, sx, sz);
		
		for(int i = 0; i < sx; i++)
				for(int k = 0; k < sz; k++)
				{
					double w = sw[i][k];
					if(w < 0) w = 0;
					if(w > 1) w = 1;
					
					double va = sa[i][k];
					double vb = sb[i][k];
					
					sa[i][k] = va * w + vb*(1-w);
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
