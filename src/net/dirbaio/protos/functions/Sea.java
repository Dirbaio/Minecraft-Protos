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

import net.dirbaio.protos.functions.FunctionTerrain;

public class Sea extends FunctionTerrain
{
	short block;
	int height;
	FunctionTerrain base;

    public Sea()
    {
    }

	public Sea(FunctionTerrain base, short block, int height)
	{
		this.base = base;
		this.block = block;
		this.height = height;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] r = base.getTerrainData(px, pz, sx, sz);
		int m = sx*sz*height;
		for(int i = 0; i < m; i++)
			if(r[i] == (short)0)
				r[i] = block;
		return r;
	}


	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
}
