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

public class Multiply2D extends Function2D
{
	public Function2D a, b;

	public Multiply2D()
	{
	}

	public Multiply2D(Function2D a, Function2D b)
	{
		this.a = a;
		this.b = b;
	}
		
	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] da = a.get2DData(px, pz, sx, sz);
		double[][] db;
		
		if(a == b)
			db = da;
		else
			db = b.get2DData(px, pz, sx, sz);
		
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sz; j++)
			{
				double na = da[i][j];
				double nb = db[i][j];
				
				da[i][j] = na*nb;
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
