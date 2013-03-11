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

public class TerrainJoin extends FunctionTerrain
{
	FunctionTerrain a, b;
	
	public TerrainJoin()
	{
	}

	public TerrainJoin(FunctionTerrain a, FunctionTerrain b)
	{
		this.a = a;
		this.b = b;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] da = a.getTerrainData(px, pz, sx, sz);
		short[] db = b.getTerrainData(px, pz, sx, sz);
		
		int len = da.length;
		
		for(int i = 0; i < len; i++)
			if(da[i] == 0)
				da[i] = db[i];

		return da;
	}


	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
	}
}
